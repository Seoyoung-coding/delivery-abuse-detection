package com.example.product.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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


    // 4. Seller가 업로드한 실제 사진 파일
    private MultipartFile image;
}