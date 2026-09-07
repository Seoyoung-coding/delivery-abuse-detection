package com.example.payment.service;

import com.example.order.domain.OrderEntity;
import com.example.order.enums.OrderStatus;
import com.example.order.repository.OrderRepository;
import com.example.payment.domain.Payment;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Payment pay(Long orderId) {

        // 주문 조회
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Order not found")
                );

        // 이미 결제된 주문인지 확인
        if (order.getStatus() != OrderStatus.ORDERED) {
            throw new IllegalStateException(
                    "Only ORDERED orders can be paid"
            );
        }

        // 결제 정보 생성
        Payment payment = new Payment();

        payment.setOrder(order);

        // 결제 금액은 주문 총 금액과 동일
        payment.setAmount(order.getTotalAmount());

        // 실제 결제가 성공했다고 가정
        payment.setStatus(PaymentStatus.SUCCESS);

        payment.setPaidAt(LocalDateTime.now());

        // 주문 상태 변경
        order.setStatus(OrderStatus.PAID);

        // 결제 저장
        return paymentRepository.save(payment);
    }
}