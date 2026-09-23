package com.example.deliverymap;

import java.time.Instant;
import java.util.Set;

// 모든 단계와 이동률은 저장된 서버 시각에서 유도한다. 타이머로 상태를 누적하지 않는다.
public final class Timeline {
    private Timeline() {}
    public static String phase(TrackingState state,Instant now) {
        if (state.status().equals("READY")) return "READY";
        if (now.isBefore(state.pickupCompletedAt())) return "PICKING_UP";
        if (now.isBefore(state.estimatedArrivalAt())) return "DELIVERING";
        if (now.isBefore(state.deliveredAt())) return "DROPPING_OFF";
        return "DELIVERED";
    }
    private static double fraction(Instant start,Instant end,Instant now) {
        double duration=end.toEpochMilli()-start.toEpochMilli();
        return duration<=0?1:Math.max(0,Math.min(1,(now.toEpochMilli()-start.toEpochMilli())/duration));
    }
    public static double progress(TrackingState state,Instant now) {
        if (state.status().equals("READY")) return 0;
        return fraction(state.pickupCompletedAt(),state.estimatedArrivalAt(),now);
    }
    // 배속 변경 시 각 단계의 의미상 경과시간을 보존한다. 픽업 중에는 이동률이 계속 0이다.
    private static double elapsedReal(TrackingState s,Instant now) {
        if (s.status().equals("READY")) return 0;
        return switch(phase(s,now)) {
            case "PICKING_UP" -> fraction(s.startedAt(),s.pickupCompletedAt(),now)*s.pickupWaitSeconds();
            case "DELIVERING" -> s.pickupWaitSeconds()+progress(s,now)*s.realTravelDurationSeconds();
            case "DROPPING_OFF" -> s.pickupWaitSeconds()+s.realTravelDurationSeconds()
                    +fraction(s.estimatedArrivalAt(),s.deliveredAt(),now)*s.dropoffWaitSeconds();
            default -> s.getRealTotalDurationSeconds();
        };
    }
    public static TrackingState withWaits(TrackingState s,double pickup,double dropoff) {
        return new TrackingState(s.trackingId(),s.mode(),s.status(),s.start(),s.destination(),s.route(),
                s.distanceMeters(),s.walkingDurationSeconds(),s.startedAt(),s.estimatedArrivalAt(),s.speedMultiplier(),
                s.routeProvider(),s.routeFetchedAt(),s.speedMetersPerSecond(),s.realTravelDurationSeconds(),
                s.pickupCompletedAt(),s.deliveredAt(),pickup,dropoff);
    }
    public static TrackingState withSpeed(TrackingState s,double speed,Instant now,boolean restart) {
        if (!Set.of(1.0,5.0,10.0,30.0,60.0).contains(speed))
            throw new IllegalArgumentException("지원 속도: 1, 5, 10, 30, 60");
        if (!restart && s.status().equals("READY")) return copy(s,"READY",speed,null,null,null,null);
        double elapsed=restart?0:elapsedReal(s,now);
        Instant start=now.minusMillis(Math.round(elapsed*1000/speed));
        Instant pickup=start.plusMillis(Math.round(s.pickupWaitSeconds()*1000/speed));
        Instant arrival=pickup.plusMillis(Math.max(1,Math.round(s.realTravelDurationSeconds()*1000/speed)));
        Instant delivered=arrival.plusMillis(Math.round(s.dropoffWaitSeconds()*1000/speed));
        TrackingState next=copy(s,"PICKING_UP",speed,start,pickup,arrival,delivered);
        return settle(next,now);
    }
    private static TrackingState copy(TrackingState s,String status,double speed,Instant start,
                                      Instant pickup,Instant arrival,Instant delivered) {
        return new TrackingState(s.trackingId(),s.mode(),status,s.start(),s.destination(),s.route(),
                s.distanceMeters(),s.walkingDurationSeconds(),start,arrival,speed,s.routeProvider(),s.routeFetchedAt(),
                s.speedMetersPerSecond(),s.realTravelDurationSeconds(),pickup,delivered,s.pickupWaitSeconds(),s.dropoffWaitSeconds());
    }
    public static TrackingState settle(TrackingState s,Instant now) {
        String status=phase(s,now);
        return status.equals(s.status())?s:copy(s,status,s.speedMultiplier(),s.startedAt(),s.pickupCompletedAt(),s.estimatedArrivalAt(),s.deliveredAt());
    }
}
