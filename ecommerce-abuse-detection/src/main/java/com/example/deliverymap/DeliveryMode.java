package com.example.deliverymap;

public enum DeliveryMode {
    WALK, MOTORCYCLE;
    public static DeliveryMode parse(String value) {
        try { return valueOf(value); }
        catch (RuntimeException e) { throw new IllegalArgumentException("지원 mode: WALK, MOTORCYCLE"); }
    }
}
