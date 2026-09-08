package com.example.abuse.dto.response;

import com.example.abuse.enums.RiskLevel;

public class CustomerAbuseResponse {

    private Long customerId;
    private long orderCount;
    private long refundCount;
    private double refundRate;
    private double score;
    private RiskLevel riskLevel;

    public CustomerAbuseResponse(
            Long customerId,
            long orderCount,
            long refundCount,
            double refundRate,
            double score,
            RiskLevel riskLevel
    ) {
        this.customerId = customerId;
        this.orderCount = orderCount;
        this.refundCount = refundCount;
        this.refundRate = refundRate;
        this.score = score;
        this.riskLevel = riskLevel;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public long getRefundCount() {
        return refundCount;
    }

    public double getRefundRate() {
        return refundRate;
    }

    public double getScore() {
        return score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }
}
