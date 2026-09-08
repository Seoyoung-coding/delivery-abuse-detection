package com.example.abuse.service;

import com.example.abuse.dto.response.CustomerAbuseResponse;
import com.example.abuse.enums.RiskLevel;
import com.example.order.repository.OrderRepository;
import com.example.refund.repository.RefundRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerAbuseService {

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;

    public CustomerAbuseService(
            OrderRepository orderRepository,
            RefundRepository refundRepository
    ) {
        this.orderRepository = orderRepository;
        this.refundRepository = refundRepository;
    }

    public double calculateScore(Long customerId) {

        long orderCount =
                orderRepository.countByCustomerId(customerId);

        long refundCount =
                refundRepository.countByCustomerId(customerId);

        // 주문한 적이 없으면 abuse score = 0
        if (orderCount == 0) {
            return 0;
        }

        // 환불률
        double refundRate =
                (double) refundCount / orderCount;

        /*
         * 주문 수가 너무 적은 고객을 과도하게 의심하지 않기 위한 보정값
         *
         * 주문 1개  -> 0.1
         * 주문 5개  -> 0.5
         * 주문 10개 이상 -> 1.0
         */
        double sampleFactor =
                Math.min(orderCount / 10.0, 1.0);

        // 환불률 기반 점수
        double rateScore =
                refundRate * sampleFactor * 80;

        // 반복 환불 자체에 대한 추가 점수
        double refundCountScore =
                Math.min(refundCount * 5.0, 20.0);

        double score =
                rateScore + refundCountScore;

        // 최대 100점
        return Math.min(score, 100.0);
    }

    public RiskLevel calculateRiskLevel(Long customerId) {

        double score =
                calculateScore(customerId);

        if (score >= 80) {
            return RiskLevel.CRITICAL;
        }

        if (score >= 60) {
            return RiskLevel.HIGH;
        }

        if (score >= 30) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.LOW;
    }

    public CustomerAbuseResponse evaluateCustomer(Long customerId) {

        long orderCount =
                orderRepository.countByCustomerId(customerId);

        long refundCount =
                refundRepository.countByCustomerId(customerId);

        double refundRate = 0;

        if (orderCount > 0) {
            refundRate =
                    (double) refundCount / orderCount;
        }

        double score =
                calculateScore(customerId);

        RiskLevel riskLevel =
                calculateRiskLevel(customerId);

        return new CustomerAbuseResponse(
                customerId,
                orderCount,
                refundCount,
                refundRate,
                score,
                riskLevel
        );
    }
}