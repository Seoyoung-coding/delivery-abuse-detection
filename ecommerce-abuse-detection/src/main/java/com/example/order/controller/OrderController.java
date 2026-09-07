package com.example.order.controller;

import com.example.order.domain.OrderEntity;
import com.example.order.dto.request.CreateOrderRequest;
import com.example.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(
            @RequestParam Long customerId,
            @RequestBody CreateOrderRequest request
    ) {
        OrderEntity order = orderService.createOrder(customerId, request);

        return ResponseEntity.ok(order);
    }
}