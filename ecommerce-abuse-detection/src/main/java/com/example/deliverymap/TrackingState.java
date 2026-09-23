package com.example.deliverymap;
import java.time.Instant;
import java.util.List;

public record TrackingState(String trackingId, String mode, String status,
                            Point start, Point destination, List<Point> route, double distanceMeters,
                            double walkingDurationSeconds, Instant startedAt, Instant estimatedArrivalAt,
                            double speedMultiplier, String routeProvider, Instant routeFetchedAt,
                            Double speedMetersPerSecond, Double realTravelDurationSeconds,
                            Instant pickupCompletedAt, Instant deliveredAt,
                            Double pickupWaitSeconds, Double dropoffWaitSeconds) {
    // 이전 저장 파일에는 대기 단계가 없었다. 복구 시 기존 시각을 변경하지 않는다.
    public TrackingState {
        if (realTravelDurationSeconds==null) realTravelDurationSeconds=walkingDurationSeconds;
        if (speedMetersPerSecond==null && realTravelDurationSeconds>0)
            speedMetersPerSecond=distanceMeters/realTravelDurationSeconds;
        if (pickupWaitSeconds==null) pickupWaitSeconds=0.0;
        if (dropoffWaitSeconds==null) dropoffWaitSeconds=0.0;
        if (pickupCompletedAt==null) pickupCompletedAt=startedAt;
        if (deliveredAt==null) deliveredAt=estimatedArrivalAt;
    }
    public TrackingState(String trackingId,String mode,String status,Point start,Point destination,
                         List<Point> route,double distanceMeters,double walkingDurationSeconds,Instant startedAt,
                         Instant estimatedArrivalAt,double speedMultiplier,String routeProvider,Instant routeFetchedAt,
                         Double speedMetersPerSecond,Double realTravelDurationSeconds) {
        this(trackingId,mode,status,start,destination,route,distanceMeters,walkingDurationSeconds,
                startedAt,estimatedArrivalAt,speedMultiplier,routeProvider,routeFetchedAt,
                speedMetersPerSecond,realTravelDurationSeconds,null,null,null,null);
    }
    public TrackingState(String trackingId,String mode,String status,Point start,Point destination,
                         List<Point> route,double distanceMeters,double walkingDurationSeconds,Instant startedAt,
                         Instant estimatedArrivalAt,double speedMultiplier,String routeProvider,Instant routeFetchedAt) {
        this(trackingId,mode,status,start,destination,route,distanceMeters,walkingDurationSeconds,
                startedAt,estimatedArrivalAt,speedMultiplier,routeProvider,routeFetchedAt,null,null);
    }
    public record Point(double longitude, double latitude) {}
    public double getRealTotalDurationSeconds() { return pickupWaitSeconds+realTravelDurationSeconds+dropoffWaitSeconds; }
    public double getDemoPlaybackDurationSeconds() { return getRealTotalDurationSeconds()/speedMultiplier; }
}
