package com.example.deliverymap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeliverySpeedConfig {
    private final double walking, motorcycle;
    public DeliverySpeedConfig(@Value("${demo.walk-speed-mps}") double walking,
                               @Value("${demo.motorcycle-speed-mps}") double motorcycle) {
        DeliveryTimingCalculator.duration(1,walking);
        DeliveryTimingCalculator.duration(1,motorcycle);
        this.walking=walking; this.motorcycle=motorcycle;
    }
    public double metersPerSecond(DeliveryMode mode) { return mode==DeliveryMode.WALK?walking:motorcycle; }
}
