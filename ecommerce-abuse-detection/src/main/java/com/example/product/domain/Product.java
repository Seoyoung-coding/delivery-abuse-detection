package com.example.product.domain;

import com.example.store.domain.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    // 1. 상품 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 2. 이 상품이 어느 Store의 상품인지 연결
    // 여러 Product가 하나의 Store에 속할 수 있음
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    // 3. 상품 이름
    @Column(nullable = false)
    private String name;


    // 4. 상품 설명
    private String description;


    // 5. 상품 가격
    @Column(nullable = false)
    private BigDecimal price;


    // 6. 상품 이미지 주소
    // 실제 이미지 자체가 아니라 이미지 URL을 저장
    private String imageUrl;


    // 7. 현재 판매 가능한 상품인지
    @Column(nullable = false)
    private boolean available = true;


    // 8. 상품 생성
    public Product(
            Store store,
            String name,
            String description,
            BigDecimal price,
            String imageUrl
    ) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = true;
    }
}