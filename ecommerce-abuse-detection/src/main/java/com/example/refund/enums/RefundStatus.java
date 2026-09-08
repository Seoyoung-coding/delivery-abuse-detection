package com.example.refund.enums;

public enum RefundStatus {

    // 고객이 환불 요청함
    REQUESTED,

    // 환불 승인
    APPROVED,

    // 환불 거절
    REJECTED,

    // 실제 환불 완료
    COMPLETED
}