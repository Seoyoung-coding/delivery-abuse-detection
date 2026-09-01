package com.example.product.dto.response;

import com.example.product.domain.Product;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
public class ProductResponse {

    // 상품 ID
    private final Long id;

    // 상품 이름
    private final String name;

    // 상품 설명
    private final String description;

    // 상품 가격
    private final BigDecimal price;

    // 상품 이미지 URL
    private final String imageUrl;

    // 현재 판매 가능 여부
    private final boolean available;


    // Product Entity → ProductResponse 변환
    public ProductResponse(Product product) {

        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.available = product.isAvailable();
    }
}