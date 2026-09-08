package com.example.abuse.controller;

import com.example.abuse.dto.response.CustomerAbuseResponse;
import com.example.abuse.service.CustomerAbuseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/abuse")
public class CustomerAbuseController {

    private final CustomerAbuseService customerAbuseService;

    public CustomerAbuseController(
            CustomerAbuseService customerAbuseService
    ) {
        this.customerAbuseService =
                customerAbuseService;
    }

    // 고객 abuse 위험도 조회
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerAbuseResponse> getCustomerRisk(
            @PathVariable Long customerId
    ) {

        CustomerAbuseResponse response =
                customerAbuseService.evaluateCustomer(customerId);

        return ResponseEntity.ok(response);
    }
}