package com.example.admin.controller;

import com.example.admin.service.AdminSellerService;
import com.example.seller.domain.Seller;
import com.example.store.domain.Store;

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

        return ResponseEntity.ok(
                sellers
        );
    }


    // =====================================================
    // Seller -> Admin Seller 관리 화면용 데이터
    // =====================================================
    private Map<String, Object> toResponse(
            Seller seller
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        // Seller가 소유한 Store 조회
        Store store =
                adminSellerService
                        .getStoreBySeller(
                                seller
                        );


        // Store에 등록된 실제 Product 개수
        long productCount =
                adminSellerService
                        .getProductCount(
                                store
                        );


        // Seller ID
        response.put(
                "sellerId",
                seller.getId()
        );


        // Seller와 연결된 Customer ID
        response.put(
                "customerId",
                seller.getCustomer().getId()
        );


        // 가입 Email
        response.put(
                "email",
                seller.getCustomer().getEmail()
        );


        // 실제 Store 이름
        response.put(
                "storeName",
                store != null
                        ? store.getName()
                        : "No Store"
        );


        // 실제 Store에 등록된 Product 개수
        response.put(
                "feeds",
                productCount
        );


        return response;
    }
}