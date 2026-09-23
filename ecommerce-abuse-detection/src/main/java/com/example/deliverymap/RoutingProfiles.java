package com.example.deliverymap;

public final class RoutingProfiles {
    private RoutingProfiles() {}
    // MVP의 오토바이는 auto-compatible 도로 경로다. 전용 프로필로 교체할 위치를 한 곳으로 모은다.
    public static String costing(DeliveryMode mode) { return mode==DeliveryMode.WALK?"pedestrian":"auto"; }
}
