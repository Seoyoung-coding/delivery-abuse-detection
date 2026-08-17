package com.example.product.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    // 1. Seller가 입력한 상품 이름
    private String name;


    // 2. 상품 설명
    private String description;


    // 3. 상품 가격
    private BigDecimal price;


    // 4. 상품 이미지 URL
    // 지금은 실제 이미지 업로드 대신 URL을 받음
    private String imageUrl;
}