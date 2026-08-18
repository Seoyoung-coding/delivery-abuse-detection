package com.example.seller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellerApplicationRequest {

    // 등록하려는 Store 이름
    private String storeName;

    // Store 설명
    private String description;

    // Store 주소
    private String address;
}