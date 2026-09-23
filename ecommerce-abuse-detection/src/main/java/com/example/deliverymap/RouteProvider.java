package com.example.deliverymap;

import java.time.Instant;
import java.util.List;
public interface RouteProvider {
    record Route(List<TrackingState.Point> points, double distanceMeters,
                 double durationSeconds, String provider, Instant fetchedAt) {}
    Route fetch();
    default Route fetch(DeliveryMode mode) {
        if (mode!=DeliveryMode.WALK) throw new IllegalArgumentException("이 공급자는 WALK만 지원합니다.");
        return fetch();
    }
}

