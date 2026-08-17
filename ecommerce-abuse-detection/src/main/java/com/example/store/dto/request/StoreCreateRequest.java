package com.example.store.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreCreateRequest {

    // 1. 사용자가 입력한 가게 이름
    private String name;


    // 2. 사용자가 입력한 가게 설명
    private String description;


    // 3. 사용자가 입력한 가게 주소
    private String address;
}