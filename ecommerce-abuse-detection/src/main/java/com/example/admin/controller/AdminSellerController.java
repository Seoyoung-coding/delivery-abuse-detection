package com.example.admin.controller;

import com.example.admin.service.AdminSellerService;
import com.example.seller.domain.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/sellers")
@RequiredArgsConstructor
public class AdminSellerController {

    private final AdminSellerService adminSellerService;


    // =====================================================
    // Admin : 실제 Seller 전체 조회
    // GET /api/admin/sellers
    // =====================================================
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSellers() {

        List<Map<String, Object>> sellers =
                adminSellerService
                        .getAllSellers()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(sellers);
    }


    // =====================================================
    // Seller -> Admin Seller 관리 화면용 데이터
    // =====================================================
    private Map<String, Object> toResponse(
            Seller seller
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "sellerId",
                seller.getId()
        );

        response.put(
                "customerId",
                seller.getCustomer().getId()
        );

        response.put(
                "email",
                seller.getCustomer().getEmail()
        );

        return response;
    }
}