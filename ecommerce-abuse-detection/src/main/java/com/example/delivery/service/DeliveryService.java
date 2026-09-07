package com.example.delivery.service;

import com.example.delivery.domain.Delivery;
import com.example.delivery.enums.DeliveryStatus;
import com.example.delivery.repository.DeliveryRepository;
import com.example.order.domain.OrderEntity;
import com.example.order.enums.OrderStatus;
import com.example.order.repository.OrderRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            OrderRepository orderRepository
    ) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
    }

    // =====================================================
    // 배송 생성
    // PAID 상태의 주문만 배송 시작 가능
    // =====================================================
    @Transactional
    public Delivery createDelivery(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Order not found")
                );

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException(
                    "Only PAID orders can start delivery"
            );
        }

        // 같은 주문에 배송이 이미 존재하는지 확인
        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException(
                    "Delivery already exists"
            );
        }

        Delivery delivery = new Delivery();

        delivery.setOrder(order);
        delivery.setStatus(DeliveryStatus.READY);

        return deliveryRepository.save(delivery);
    }


    // =====================================================
    // 기사 배정
    // READY → ASSIGNED
    // =====================================================
    @Transactional
    public Delivery assign(Long deliveryId) {

        Delivery delivery = getDelivery(deliveryId);

        if (delivery.getStatus() != DeliveryStatus.READY) {
            throw new IllegalStateException(
                    "Only READY deliveries can be assigned"
            );
        }

        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setAssignedAt(LocalDateTime.now());

        return delivery;
    }


    // =====================================================
    // 상품 픽업
    // ASSIGNED → PICKED_UP
    // =====================================================
    @Transactional
    public Delivery pickup(Long deliveryId) {

        Delivery delivery = getDelivery(deliveryId);

        if (delivery.getStatus() != DeliveryStatus.ASSIGNED) {
            throw new IllegalStateException(
                    "Only ASSIGNED deliveries can be picked up"
            );
        }

        delivery.setStatus(DeliveryStatus.PICKED_UP);
        delivery.setPickedUpAt(LocalDateTime.now());

        return delivery;
    }


    // =====================================================
    // 배송 시작
    // PICKED_UP → DELIVERING
    // =====================================================
    @Transactional
    public Delivery startDelivery(Long deliveryId) {

        Delivery delivery = getDelivery(deliveryId);

        if (delivery.getStatus() != DeliveryStatus.PICKED_UP) {
            throw new IllegalStateException(
                    "Only PICKED_UP deliveries can start"
            );
        }

        delivery.setStatus(DeliveryStatus.DELIVERING);

        return delivery;
    }


    // =====================================================
    // 배송 완료
    // DELIVERING → DELIVERED
    // Order도 PAID → DELIVERED
    // =====================================================
    @Transactional
    public Delivery complete(Long deliveryId) {

        Delivery delivery = getDelivery(deliveryId);

        if (delivery.getStatus() != DeliveryStatus.DELIVERING) {
            throw new IllegalStateException(
                    "Only DELIVERING deliveries can be completed"
            );
        }

        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredAt(LocalDateTime.now());

        // 주문 전체 상태도 배송 완료로 변경
        OrderEntity order = delivery.getOrder();
        order.setStatus(OrderStatus.DELIVERED);

        return delivery;
    }


    // 배송 조회 공통 메서드
    private Delivery getDelivery(Long deliveryId) {

        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Delivery not found")
                );
    }
}