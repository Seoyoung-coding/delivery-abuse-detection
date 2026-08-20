package com.example.seller.controller;

import com.example.customer.domain.Customer;
import com.example.customer.service.CustomerService;
import com.example.seller.dto.SellerApplicationRequest;
import com.example.seller.dto.response.SellerApplicationStatusResponse;
import com.example.seller.service.SellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller-applications")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = {
                "Authorization",
                "Content-Type"
        }
)
public class SellerApplicationController {

    // =========================
    // 1. 필요한 Service
    // =========================

    private final SellerApplicationService sellerApplicationService;
    private final CustomerService customerService;


    // =========================
    // 2. Store 등록 신청
    //
    // POST
    // /api/seller-applications
    // =========================

    @PostMapping
    public ResponseEntity<String> apply(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody SellerApplicationRequest request
    ) {

        // JWT를 이용해서
        // 현재 로그인한 Customer 찾기
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // Seller 등록 신청
        sellerApplicationService.apply(
                customer,
                request.getStoreName(),
                request.getDescription(),
                request.getAddress()
        );


        // 신청 성공 응답
        return ResponseEntity.ok(
                "Seller application submitted successfully."
        );
    }


    // =========================
    // 3. 현재 사용자의
    // Seller 신청 상태 조회
    //
    // GET
    // /api/seller-applications/me/status
    // =========================

    @GetMapping("/me/status")
    public ResponseEntity<SellerApplicationStatusResponse> getMyStatus(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        // JWT를 이용해서
        // 현재 로그인한 Customer 찾기
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 현재 Seller 신청 상태 조회
        //
        // NONE
        // PENDING
        // REJECTED
        // APPROVED
        String status =
                sellerApplicationService
                        .getApplicationStatus(
                                customer
                        );


        // JSON 형태로 상태 반환
        return ResponseEntity.ok(
                new SellerApplicationStatusResponse(
                        status
                )
        );
    }
}