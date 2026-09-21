package com.example.abuse.domain;

import com.example.customer.domain.Customer;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 조회가 가능한 로직 만들기
@Entity
@Table(name = "customer_risk_profiles")
public class CustomerRiskProfile {

    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // 총 주문 횟수
    @Column(nullable = false)
    private long totalOrderCount = 0;

    // 총 주문 금액
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalOrderAmount = BigDecimal.ZERO;

    // 총 환불 횟수
    @Column(nullable = false)
    private long totalRefundCount = 0;

    // 총 환불 금액
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalRefundAmount = BigDecimal.ZERO;

    private LocalDateTime updatedAt;

    public CustomerRiskProfile() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(long totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public BigDecimal getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public long getTotalRefundCount() {
        return totalRefundCount;
    }

    public void setTotalRefundCount(long totalRefundCount) {
        this.totalRefundCount = totalRefundCount;
    }

    public BigDecimal getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}