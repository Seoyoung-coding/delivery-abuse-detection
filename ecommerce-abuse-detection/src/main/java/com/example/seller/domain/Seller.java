package com.example.seller.domain;

import com.example.customer.domain.Customer;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Seller {

    // =========================
    // Seller ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // Seller와 연결된 Customer
    // =========================

    @OneToOne
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            unique = true
    )
    private Customer customer;


    // =========================
    // Seller 계정 Email
    //
    // Customer의 email을 복사해서 저장
    // DB에서 Seller 식별을 쉽게 하기 위함
    // =========================

    @Column(
            nullable = false
    )
    private String email;


    // =========================
    // Seller 생성
    // =========================

    public Seller(
            Customer customer
    ) {

        this.customer =
                customer;

        // 연결된 Customer의 email 자동 저장
        this.email =
                customer.getEmail();
    }
}