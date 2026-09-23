package com.example.deliverymap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.*;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class TrackingService {
    private final RouteProvider provider;
    private final ObjectMapper mapper;
    private final Path file;
    private final double defaultSpeed;
    private final DeliverySpeedConfig deliverySpeeds;
    private final DeliveryTimingConfig timing;
    private final java.util.Map<DeliveryMode,RouteProvider.Route> routes=new java.util.EnumMap<>(DeliveryMode.class);
    private TrackingState state;
    public TrackingService(RouteProvider provider, ObjectMapper mapper,
                           @Value("${demo.state-file}") String file, @Value("${demo.default-speed}") double defaultSpeed,
                           DeliverySpeedConfig deliverySpeeds, DeliveryTimingConfig timing) {
        this.provider = provider; this.mapper = mapper; this.file = Path.of(file); this.defaultSpeed = defaultSpeed;
        this.deliverySpeeds=deliverySpeeds;this.timing=timing;
        if (Files.exists(this.file)) {
            try {
                state = mapper.readerFor(TrackingState.class)
                        .without(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .readValue(this.file.toFile());
                routes.put(DeliveryMode.parse(state.mode()),new RouteProvider.Route(state.route(),state.distanceMeters(),
                        state.realTravelDurationSeconds(),state.routeProvider(),state.routeFetchedAt()));
            }
            catch (IOException e) { throw new IllegalStateException("저장된 추적 파일을 읽을 수 없습니다. 백업 후 파일을 확인해 주세요."); }
        }
    }
    public synchronized TrackingState current() {
        if (state == null) {
            save(newTracking(DeliveryMode.WALK,defaultSpeed));
        }
        TrackingState settled = Timeline.settle(state, Instant.now());
        if (settled != state) save(settled);
        return state;
    }
    public synchronized TrackingState start() {
        TrackingState current = current();
        if (current.status().equals("READY") || current.status().equals("DELIVERED"))
            save(Timeline.withSpeed(Timeline.withWaits(current,timing.pickupSeconds(),timing.dropoffSeconds()), current.speedMultiplier(), Instant.now(), true));
        return state;
    }
    public synchronized TrackingState restart() {
        TrackingState current = current();
        save(Timeline.withSpeed(Timeline.withWaits(current,timing.pickupSeconds(),timing.dropoffSeconds()), current.speedMultiplier(), Instant.now(), true));
        return state;
    }
    public synchronized TrackingState speed(double value) {
        save(Timeline.withSpeed(current(), value, Instant.now(), false)); return state;
    }
    public synchronized TrackingState mode(String mode) {
        DeliveryMode selected=DeliveryMode.parse(mode);
        if (state!=null && state.mode().equals(selected.name())) return current();
        // 새 경로를 얻는 데 실패하면 기존 tracking은 그대로 유지한다.
        TrackingState next=newTracking(selected,state==null?defaultSpeed:state.speedMultiplier());
        save(next); return state;
    }
    private TrackingState newTracking(DeliveryMode mode,double multiplier) {
        RouteProvider.Route route=routes.computeIfAbsent(mode,provider::fetch);
        var points=route.points();
        double speed=deliverySpeeds.metersPerSecond(mode);
        double duration=DeliveryTimingCalculator.duration(route.distanceMeters(),speed);
        return new TrackingState(UUID.randomUUID().toString(),mode.name(),"READY",points.get(0),
                points.get(points.size()-1),points,route.distanceMeters(),duration,null,null,multiplier,
                route.provider(),route.fetchedAt(),speed,duration,null,null,timing.pickupSeconds(),timing.dropoffSeconds());
    }
    private void save(TrackingState next) {
        try {
            Path absolute = file.toAbsolutePath();
            Files.createDirectories(absolute.getParent());
            Path temporary = Files.createTempFile(absolute.getParent(), "tracking-", ".tmp");
            try {
                mapper.writeValue(temporary.toFile(), next);
                try { Files.move(temporary, absolute, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
                catch (AtomicMoveNotSupportedException e) { Files.move(temporary, absolute, StandardCopyOption.REPLACE_EXISTING); }
            } finally { Files.deleteIfExists(temporary); }
            state = next;
        } catch (IOException e) { throw new IllegalStateException("추적 상태를 저장할 수 없습니다."); }
    }
}
