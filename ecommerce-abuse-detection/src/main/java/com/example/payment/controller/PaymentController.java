package com.example.payment.controller;

import com.example.payment.domain.Payment;
import com.example.payment.dto.request.PaymentResponse;
import com.example.payment.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 결제 실행
    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> pay(
            @PathVariable Long orderId
    ) {

        Payment payment = paymentService.pay(orderId);

        return ResponseEntity.ok(
                new PaymentResponse(payment)
        );
    }
}