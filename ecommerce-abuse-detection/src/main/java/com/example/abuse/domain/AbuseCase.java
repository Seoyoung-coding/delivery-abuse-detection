package com.example.abuse.domain;

import com.example.abuse.enums.AbuseCaseStatus;
import com.example.abuse.enums.RiskLevel;
import com.example.customer.domain.Customer;
import com.example.refund.domain.Refund;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "abuse_cases")
public class AbuseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 고객에 대한 케이스인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // 어떤 환불 요청 때문에 생성된 케이스인지
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id", nullable = false, unique = true)
    private Refund refund;

    // 케이스 생성 당시 Abuse Score
    @Column(nullable = false)
    private double abuseScore;

    // 케이스 생성 당시 Risk Level
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    // Admin 처리 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbuseCaseStatus status = AbuseCaseStatus.OPEN;

    // Admin 메모
    @Column(length = 1000)
    private String adminNote;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public AbuseCase() {
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public double getAbuseScore() {
        return abuseScore;
    }

    public void setAbuseScore(double abuseScore) {
        this.abuseScore = abuseScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public AbuseCaseStatus getStatus() {
        return status;
    }

    public void setStatus(AbuseCaseStatus status) {
        this.status = status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}