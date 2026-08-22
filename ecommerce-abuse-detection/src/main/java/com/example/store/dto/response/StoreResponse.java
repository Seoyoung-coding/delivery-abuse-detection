package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponse {

    private Long id;

    private String name;

    private String description;

    private String address;

    private String imageUrl;
}