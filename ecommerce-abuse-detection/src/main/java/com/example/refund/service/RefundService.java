package com.example.refund.service;

import com.example.abuse.dto.response.CustomerAbuseResponse;
import com.example.abuse.service.AbuseCaseService;
import com.example.abuse.service.CustomerAbuseService;
import com.example.order.domain.OrderEntity;
import com.example.order.enums.OrderStatus;
import com.example.order.repository.OrderRepository;
import com.example.payment.domain.Payment;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.repository.PaymentRepository;
import com.example.refund.domain.Refund;
import com.example.refund.dto.RefundResponse;
import com.example.refund.dto.request.RefundRequest;
import com.example.refund.enums.RefundResolution;
import com.example.refund.enums.RefundStatus;
import com.example.refund.repository.RefundRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerAbuseService customerAbuseService;
    private final AbuseCaseService abuseCaseService;
    private final ReturnlessDecisionService returnlessDecisionService;

    public RefundService(
            RefundRepository refundRepository,
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            CustomerAbuseService customerAbuseService,
            AbuseCaseService abuseCaseService,
            ReturnlessDecisionService returnlessDecisionService
    ) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.customerAbuseService = customerAbuseService;
        this.abuseCaseService = abuseCaseService;
        this.returnlessDecisionService = returnlessDecisionService;
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

        // 2. 같은 주문에 이미 환불 요청이 있는지 확인
        if (refundRepository.existsByOrderId(orderId)) {
            throw new IllegalStateException(
                    "Refund request already exists for this order"
            );
        }

        // 3. 환불 생성
        Refund refund = new Refund();

        refund.setOrder(order);
        refund.setAmount(order.getTotalAmount());
        refund.setReason(request.getReason());
        refund.setStatus(RefundStatus.REQUESTED);

        Refund savedRefund =
                refundRepository.save(refund);

        // 4. 주문 상태 변경
        order.setStatus(OrderStatus.REFUND_REQUESTED);

        // 5. 고객 ID 조회
        Long customerId =
                order.getCustomer().getId();

        // 6. Abuse Score 계산
        CustomerAbuseResponse abuse =
                customerAbuseService.evaluateCustomer(customerId);

        // 7. Return / Returnless 판단
        RefundResolution resolution =
                returnlessDecisionService.decide(
                        order.getTotalAmount(),
                        abuse.getScore()
                );

        savedRefund.setResolution(resolution);

        // 8. Abuse Case 자동 생성
        abuseCaseService.createCase(
                order.getCustomer(),
                savedRefund,
                abuse
        );

        // 9. RETURNLESS이면 바로 환불 완료
        if (resolution == RefundResolution.RETURNLESS) {

            Payment payment =
                    paymentRepository.findByOrderId(orderId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Payment not found"
                                    )
                            );

            // 결제 상태 환불 완료
            payment.setStatus(PaymentStatus.REFUNDED);

            // 환불 상태 완료
            savedRefund.setStatus(RefundStatus.COMPLETED);
            savedRefund.setCompletedAt(LocalDateTime.now());

            // 주문도 환불 완료
            order.setStatus(OrderStatus.REFUNDED);
        }

        /*
         * RETURN_REQUIRED이면
         * 지금은 REQUESTED 상태로 그대로 둔다.
         *
         * 이후 반품 배송 로직을 연결해서
         * 상품 회수가 완료되었을 때
         * COMPLETED / REFUNDED 처리한다.
         */

        // 10. 결과 반환
        return new RefundResponse(
                savedRefund,
                abuse.getScore(),
                abuse.getRiskLevel()
        );
    }
}