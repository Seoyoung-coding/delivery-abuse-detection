package com.example.seller.domain;

import com.example.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Seller {

    // 1. Seller의 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 2. 어떤 Customer가 Seller인지 연결
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;


    // 3. Seller 생성
    public Seller(Customer customer) {
        this.customer = customer;
    }
}