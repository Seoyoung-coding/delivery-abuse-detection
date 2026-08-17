package com.example.store.domain;

import com.example.seller.domain.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor
public class Store {

    // 1. 가게 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 2. 이 가게를 소유한 Seller
    @OneToOne
    @JoinColumn(
            name = "seller_id",
            nullable = false,
            unique = true
    )
    private Seller seller;


    // 3. 가게 이름
    @Column(nullable = false)
    private String name;


    // 4. 가게 설명
    private String description;


    // 5. 가게 주소
    private String address;


    // 6. 가게 생성자
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