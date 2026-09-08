package com.example.refund.dto;

import com.example.abuse.enums.RiskLevel;
import com.example.refund.domain.Refund;

import java.math.BigDecimal;

public class RefundResponse {

    private Long refundId;
    private Long orderId;

    private BigDecimal amount;

    private String refundStatus;

    private double abuseScore;
    private RiskLevel riskLevel;

    public RefundResponse(
            Refund refund,
            double abuseScore,
            RiskLevel riskLevel
    ) {
        this.refundId = refund.getId();
        this.orderId = refund.getOrder().getId();
        this.amount = refund.getAmount();
        this.refundStatus = refund.getStatus().name();
        this.abuseScore = abuseScore;
        this.riskLevel = riskLevel;
    }

    public Long getRefundId() {
        return refundId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public double getAbuseScore() {
        return abuseScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }
}