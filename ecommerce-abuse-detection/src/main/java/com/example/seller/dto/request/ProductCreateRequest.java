package com.example.seller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequest {

    // 상품 이름
    private String name;

    // 상품 설명
    private String description;

    // 상품 가격
    private BigDecimal price;

    // 상품 이미지 파일
    private MultipartFile image;
}