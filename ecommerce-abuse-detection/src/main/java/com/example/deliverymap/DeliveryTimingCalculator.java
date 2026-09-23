package com.example.deliverymap;

// 실제 배송 속도와 데모 배속을 분리한다. 시간 상태의 최종 생성은 backend에서만 한다.
public final class DeliveryTimingCalculator {
    private DeliveryTimingCalculator() {}
    public static double duration(double distanceMeters,double speedMetersPerSecond) {
        if (!Double.isFinite(distanceMeters) || distanceMeters<=0 ||
                !Double.isFinite(speedMetersPerSecond) || speedMetersPerSecond<=0)
            throw new IllegalArgumentException("거리와 실제 이동속도는 유한한 양수여야 합니다.");
        return distanceMeters/speedMetersPerSecond;
    }
}
