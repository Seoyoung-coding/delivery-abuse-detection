package com.example.product.controller;

import com.example.product.dto.request.ProductCreateRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // =========================
    // Seller 상품 등록
    // =========================
    @PostMapping
    public ResponseEntity<String> createProduct(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @ModelAttribute
            ProductCreateRequest request
    ) {

        productService.createProduct(
                authorizationHeader,
                request
        );

        return ResponseEntity.ok(
                "상품 등록 성공"
        );
    }


    // =========================
    // Seller : 본인 Store 상품 목록 조회
    // =========================
    @GetMapping("/my-store")
    public ResponseEntity<List<ProductResponse>> getMyStoreProducts(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        List<ProductResponse> products =
                productService.getMyStoreProducts(
                        authorizationHeader
                );

        return ResponseEntity.ok(
                products
        );
    }


    // =========================
    // Public : 특정 Store 상품 목록 조회
    // Customer / 다른 Seller도 조회 가능
    // =========================
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductResponse>> getStoreProducts(

            @PathVariable
            Long storeId
    ) {

        List<ProductResponse> products =
                productService.getStoreProducts(
                        storeId
                );

        return ResponseEntity.ok(
                products
        );
    }
}