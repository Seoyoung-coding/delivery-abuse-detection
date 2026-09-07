package com.example.delivery.controller;

import com.example.delivery.domain.Delivery;
import com.example.delivery.dto.DeliveryResponse;
import com.example.delivery.service.DeliveryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(
            DeliveryService deliveryService
    ) {
        this.deliveryService = deliveryService;
    }


    // 배송 생성
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<DeliveryResponse> createDelivery(
            @PathVariable Long orderId
    ) {

        Delivery delivery =
                deliveryService.createDelivery(orderId);

        return ResponseEntity.ok(
                new DeliveryResponse(delivery)
        );
    }


    // 기사 배정
    @PostMapping("/{deliveryId}/assign")
    public ResponseEntity<DeliveryResponse> assign(
            @PathVariable Long deliveryId
    ) {

        Delivery delivery =
                deliveryService.assign(deliveryId);

        return ResponseEntity.ok(
                new DeliveryResponse(delivery)
        );
    }


    // 상품 픽업
    @PostMapping("/{deliveryId}/pickup")
    public ResponseEntity<DeliveryResponse> pickup(
            @PathVariable Long deliveryId
    ) {

        Delivery delivery =
                deliveryService.pickup(deliveryId);

        return ResponseEntity.ok(
                new DeliveryResponse(delivery)
        );
    }


    // 배송 시작
    @PostMapping("/{deliveryId}/start")
    public ResponseEntity<DeliveryResponse> startDelivery(
            @PathVariable Long deliveryId
    ) {

        Delivery delivery =
                deliveryService.startDelivery(deliveryId);

        return ResponseEntity.ok(
                new DeliveryResponse(delivery)
        );
    }


    // 배송 완료
    @PostMapping("/{deliveryId}/complete")
    public ResponseEntity<DeliveryResponse> complete(
            @PathVariable Long deliveryId
    ) {

        Delivery delivery =
                deliveryService.complete(deliveryId);

        return ResponseEntity.ok(
                new DeliveryResponse(delivery)
        );
    }
}