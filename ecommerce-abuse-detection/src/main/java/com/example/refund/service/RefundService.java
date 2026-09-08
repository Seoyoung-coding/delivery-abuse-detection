package com.example.refund.service;

import com.example.abuse.dto.response.CustomerAbuseResponse;
import com.example.abuse.service.AbuseCaseService;
import com.example.abuse.service.CustomerAbuseService;
import com.example.order.domain.OrderEntity;
import com.example.order.enums.OrderStatus;
import com.example.order.repository.OrderRepository;
import com.example.refund.domain.Refund;
import com.example.refund.dto.request.RefundRequest;
import com.example.refund.dto.RefundResponse;
import com.example.refund.enums.RefundStatus;
import com.example.refund.repository.RefundRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final CustomerAbuseService customerAbuseService;
    private final AbuseCaseService abuseCaseService;

    public RefundService(
            RefundRepository refundRepository,
            OrderRepository orderRepository,
            CustomerAbuseService customerAbuseService,
            AbuseCaseService abuseCaseService
    ) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.customerAbuseService = customerAbuseService;
        this.abuseCaseService = abuseCaseService;
    }

    @Transactional
    public RefundResponse requestRefund(
            Long orderId,
            RefundRequest request
    ) {

        // 1. 주문 조회
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Order not found")
                );

        // 2. 환불 생성
        Refund refund = new Refund();

        refund.setOrder(order);
        refund.setAmount(order.getTotalAmount());
        refund.setReason(request.getReason());
        refund.setStatus(RefundStatus.REQUESTED);

        Refund savedRefund =
                refundRepository.save(refund);

        // 3. 주문 상태 변경
        order.setStatus(OrderStatus.REFUND_REQUESTED);

        // 4. 고객의 현재 abuse 위험도 계산
        Long customerId =
                order.getCustomer().getId();

        CustomerAbuseResponse abuse =
                customerAbuseService.evaluateCustomer(customerId);

        // Abuse Case 자동 생성
        abuseCaseService.createCase(
                order.getCustomer(),
                savedRefund,
                abuse
        );

        // 5. 환불 정보 + abuse 결과 반환
        return new RefundResponse(
                savedRefund,
                abuse.getScore(),
                abuse.getRiskLevel()
        );
    }
}