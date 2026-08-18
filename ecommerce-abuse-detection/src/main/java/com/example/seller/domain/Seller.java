package com.example.seller.domain;

import com.example.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            unique = true
    )
    private Customer customer;

    public Seller(Customer customer) {
        this.customer = customer;
    }
}