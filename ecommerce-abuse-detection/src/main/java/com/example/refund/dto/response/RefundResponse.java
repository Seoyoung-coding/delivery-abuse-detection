package com.example.refund.dto;

import com.example.abuse.enums.RiskLevel;
import com.example.refund.domain.Refund;

import java.math.BigDecimal;

public class RefundResponse {

    private Long refundId;
    private Long orderId;

    // 환불 금액
    private BigDecimal amount;

    // REQUESTED / COMPLETED 등 환불 진행 상태
    private String refundStatus;

    // RETURN_REQUIRED / RETURNLESS
    private String resolution;

    // 고객 Abuse Score
    private double abuseScore;

    // LOW / MEDIUM / HIGH / CRITICAL
    private RiskLevel riskLevel;


    public RefundResponse(
            Refund refund,
            double abuseScore,
            RiskLevel riskLevel
    ) {

        this.refundId = refund.getId();
        this.orderId = refund.getOrder().getId();

        this.amount = refund.getAmount();

        this.refundStatus =
                refund.getStatus().name();

        this.resolution =
                refund.getResolution().name();

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

    public String getResolution() {
        return resolution;
    }

    public double getAbuseScore() {
        return abuseScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }
}