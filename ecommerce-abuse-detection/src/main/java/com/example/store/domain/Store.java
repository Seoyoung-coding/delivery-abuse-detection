package com.example.store.domain;

import com.example.seller.domain.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Store {

    // =========================
    // 1. Store 고유 ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // 2. Store를 소유한 Seller
    // =========================

    @OneToOne
    @JoinColumn(
            name = "seller_id",
            nullable = false,
            unique = true
    )
    private Seller seller;


    // =========================
    // 3. Store 이름
    // =========================

    @Column(nullable = false)
    private String name;


    // =========================
    // 4. Store 설명
    // =========================

    private String description;


    // =========================
    // 5. Store 주소
    // =========================

    private String address;


    // =========================
    // 6. Store 생성
    // =========================

    public Store(
            Seller seller,
            String name,
            String description,
            String address
    ) {

        this.seller = seller;
        this.name = name;
        this.description = description;
        this.address = address;
    }
}