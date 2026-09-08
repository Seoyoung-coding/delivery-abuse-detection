package com.example.refund.controller;

import com.example.refund.dto.request.RefundRequest;
import com.example.refund.dto.RefundResponse;
import com.example.refund.service.RefundService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(
            RefundService refundService
    ) {
        this.refundService = refundService;
    }

    // 환불 요청
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<RefundResponse> requestRefund(
            @PathVariable Long orderId,
            @RequestBody RefundRequest request
    ) {

        RefundResponse response =
                refundService.requestRefund(
                        orderId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}