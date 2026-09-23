package com.example.deliverymap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeliveryTimingConfig {
    private final double pickupSeconds,dropoffSeconds;
    public DeliveryTimingConfig(@Value("${demo.pickup-wait-seconds}") double pickupSeconds,
                                @Value("${demo.dropoff-wait-seconds}") double dropoffSeconds) {
        if (!Double.isFinite(pickupSeconds)||pickupSeconds<0||!Double.isFinite(dropoffSeconds)||dropoffSeconds<0)
            throw new IllegalArgumentException("대기시간은 0 이상의 유한한 값이어야 합니다.");
        this.pickupSeconds=pickupSeconds;this.dropoffSeconds=dropoffSeconds;
    }
    public double pickupSeconds(){return pickupSeconds;}
    public double dropoffSeconds(){return dropoffSeconds;}
}

