package com.example.deliverymap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;

@Component
public class ValhallaWalkingRouteProvider implements RouteProvider {
    private final ObjectMapper mapper;
    private final String endpoint;
    private final double latitude, longitude;
    private final List<Integer> bearings, radii;
    private final String destinationLatitude, destinationLongitude;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
    public ValhallaWalkingRouteProvider(ObjectMapper mapper,
                                        @Value("${demo.valhalla-url}") String endpoint,
                                        @Value("${demo.start-latitude}") double latitude,
                                        @Value("${demo.start-longitude}") double longitude,
                                        @Value("${demo.candidate-bearings}") List<Integer> bearings,
                                        @Value("${demo.candidate-radii}") List<Integer> radii,
                                        @Value("${demo.destination-latitude:}") String destinationLatitude,
                                        @Value("${demo.destination-longitude:}") String destinationLongitude) {
        this.mapper = mapper; this.endpoint = endpoint; this.latitude = latitude; this.longitude = longitude;
        this.bearings = bearings; this.radii = radii;
        this.destinationLatitude = destinationLatitude; this.destinationLongitude = destinationLongitude;
    }
    public Route fetch() {
        return fetch(DeliveryMode.WALK);
    }
    @Override public Route fetch(DeliveryMode mode) {
        // 후보 위치만 계산한다. 반환 경로에는 API의 실제 보행 좌표만 사용한다.
        long deadline = System.nanoTime() + Duration.ofSeconds(50).toNanos();
        List<TrackingState.Point> candidates = new ArrayList<>();
        if (!destinationLatitude.isBlank() || !destinationLongitude.isBlank()) {
            try {
                double lat = Double.parseDouble(destinationLatitude), lng = Double.parseDouble(destinationLongitude);
                if (!Double.isFinite(lat) || !Double.isFinite(lng) || Math.abs(lat)>90 || Math.abs(lng)>180)
                    throw new NumberFormatException();
                candidates.add(new TrackingState.Point(lng,lat));
            } catch (NumberFormatException e) { throw new IllegalStateException("목적지 위도·경도를 모두 올바르게 설정해 주세요."); }
        } else for (int radius : radii) for (int bearing : bearings) candidates.add(offset(latitude, longitude, radius, bearing));
        boolean firstRequest = true;
        for (TrackingState.Point candidate : candidates) {
            if (System.nanoTime() > deadline) throw new IllegalStateException("보행 경로 탐색 시간이 초과되었습니다. 다시 시도해 주세요.");
            try {
                // 공개 데모 서버에 병렬 요청을 보내지 않고 요청 사이 간격을 둔다.
                if (!firstRequest) Thread.sleep(1100);
                firstRequest = false;
                URI uri = requestUri(candidate,mode);
                HttpResponse<String> response = client.send(HttpRequest.newBuilder(uri)
                        .timeout(Duration.ofSeconds(8)).GET().build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 429)
                    throw new IllegalStateException("Valhalla 공개 서버의 요청 한도입니다. 잠시 뒤 다시 시도해 주세요.");
                if (response.statusCode() >= 500)
                    throw new IllegalStateException("Valhalla 공개 서버가 응답하지 않습니다 (HTTP " + response.statusCode() + ").");
                JsonNode root = mapper.readTree(response.body());
                int routingError = root.path("error_code").asInt(-1);
                String code = root.path("code").asText();
                if (Set.of(170,171,441,442).contains(routingError) || code.equals("NoRoute") || code.equals("NoSegment")) continue;
                if (response.statusCode() != 200 || !code.equals("Ok"))
                    throw new IllegalStateException("Valhalla 보행 API 오류 (HTTP " + response.statusCode() + "). 서버 주소와 요청 설정을 확인해 주세요.");
                JsonNode route = root.path("routes").path(0);
                // OSRM 응답 형식의 distance는 미터, duration은 초다.
                double distance = route.path("distance").asDouble();
                if (!Double.isFinite(distance) || distance < 900 || distance > 1100) continue;
                if (!route.path("geometry").path("type").asText().equals("LineString"))
                    throw new IllegalStateException("Valhalla가 GeoJSON 보행 경로를 반환하지 않았습니다.");
                List<TrackingState.Point> points = new ArrayList<>();
                for (JsonNode pair : route.path("geometry").path("coordinates")) {
                    if (pair.size() < 2 || !pair.get(0).isNumber() || !pair.get(1).isNumber())
                        throw new IllegalStateException("경로 좌표 형식이 올바르지 않습니다.");
                    double lng = pair.get(0).asDouble(), lat = pair.get(1).asDouble();
                    if (!Double.isFinite(lng) || !Double.isFinite(lat) || Math.abs(lng) > 180 || Math.abs(lat) > 90)
                        throw new IllegalStateException("경로 좌표 범위가 올바르지 않습니다.");
                    points.add(new TrackingState.Point(lng, lat));
                }
                double duration = route.path("duration").asDouble();
                if (points.size() < 2 || !Double.isFinite(duration) || duration <= 0) continue;
                return new Route(List.copyOf(points), distance, duration, "Valhalla / "+RoutingProfiles.costing(mode)+" (OpenStreetMap)", Instant.now());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); throw new IllegalStateException("경로 요청이 중단되었습니다.");
            } catch (java.io.IOException e) {
                throw new IllegalStateException("Valhalla 응답을 읽을 수 없습니다. 네트워크와 공개 서버 상태를 확인해 주세요.");
            }
        }
        throw new IllegalStateException("900~1100m 보행 경로를 찾지 못했습니다. 후보 반경·방향을 조정해 주세요.");
    }
    URI requestUri(TrackingState.Point destination) throws java.io.IOException {
        return requestUri(destination,DeliveryMode.WALK);
    }
    URI requestUri(TrackingState.Point destination,DeliveryMode mode) throws java.io.IOException {
        // Valhalla 엔진을 사용하되 좌표 해독 오류를 피하기 위해 OSRM/GeoJSON 출력 형식을 요청한다.
        Map<String,Object> request = Map.of(
                "locations", List.of(Map.of("lat",latitude,"lon",longitude,"search_cutoff",100),
                        Map.of("lat",destination.latitude(),"lon",destination.longitude(),"search_cutoff",100)),
                "costing", RoutingProfiles.costing(mode), "format", "osrm", "shape_format", "geojson", "units", "kilometers");
        String json = mapper.writeValueAsString(request);
        return URI.create(endpoint + "?json=" + URLEncoder.encode(json, StandardCharsets.UTF_8));
    }
    static TrackingState.Point offset(double lat, double lng, double meters, double degrees) {
        double a = meters / 6371008.8, b = Math.toRadians(degrees), phi = Math.toRadians(lat), lambda = Math.toRadians(lng);
        double nextPhi = Math.asin(Math.sin(phi)*Math.cos(a) + Math.cos(phi)*Math.sin(a)*Math.cos(b));
        double nextLambda = lambda + Math.atan2(Math.sin(b)*Math.sin(a)*Math.cos(phi), Math.cos(a)-Math.sin(phi)*Math.sin(nextPhi));
        return new TrackingState.Point(Math.toDegrees(nextLambda), Math.toDegrees(nextPhi));
    }
}
