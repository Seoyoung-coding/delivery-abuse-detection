package com.example.refund.enums;

public enum RefundResolution {

    PENDING,

    // 상품을 돌려받아야 환불
    RETURN_REQUIRED,

    // 상품을 돌려받지 않고 환불
    RETURNLESS
}