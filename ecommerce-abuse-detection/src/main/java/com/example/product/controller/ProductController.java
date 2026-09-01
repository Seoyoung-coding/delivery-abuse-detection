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

            // 1. 로그인한 Seller의 JWT
            @RequestHeader("Authorization")
            String authorizationHeader,

            // 2. 상품정보 + 이미지 파일 받기
            @ModelAttribute
            ProductCreateRequest request
    ) {

        // 3. Service에서 상품 등록
        productService.createProduct(
                authorizationHeader,
                request
        );

        // 4. 성공 응답
        return ResponseEntity.ok(
                "상품 등록 성공"
        );
    }

    // =========================
    // Seller Store 상품 목록 조회
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

        return ResponseEntity.ok(products);
    }
}