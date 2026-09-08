package com.example.abuse.dto.response;

import com.example.abuse.domain.AbuseCase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AbuseCaseResponse {

    private Long caseId;
    private Long customerId;
    private Long refundId;

    private double abuseScore;
    private String riskLevel;
    private String status;

    private BigDecimal refundAmount;
    private String refundReason;

    private String adminNote;
    private LocalDateTime createdAt;

    public AbuseCaseResponse(AbuseCase abuseCase) {

        this.caseId = abuseCase.getId();

        this.customerId =
                abuseCase.getCustomer().getId();

        this.refundId =
                abuseCase.getRefund().getId();

        this.abuseScore =
                abuseCase.getAbuseScore();

        this.riskLevel =
                abuseCase.getRiskLevel().name();

        this.status =
                abuseCase.getStatus().name();

        this.refundAmount =
                abuseCase.getRefund().getAmount();

        this.refundReason =
                abuseCase.getRefund().getReason();

        this.adminNote =
                abuseCase.getAdminNote();

        this.createdAt =
                abuseCase.getCreatedAt();
    }

    public Long getCaseId() {
        return caseId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getRefundId() {
        return refundId;
    }

    public double getAbuseScore() {
        return abuseScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}