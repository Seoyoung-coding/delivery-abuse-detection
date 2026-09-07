package com.example.payment.dto.request;

import com.example.payment.domain.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String paymentStatus;
    private String orderStatus;
    private LocalDateTime paidAt;

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getId();
        this.orderId = payment.getOrder().getId();
        this.amount = payment.getAmount();
        this.paymentStatus = payment.getStatus().name();
        this.orderStatus = payment.getOrder().getStatus().name();
        this.paidAt = payment.getPaidAt();
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
}