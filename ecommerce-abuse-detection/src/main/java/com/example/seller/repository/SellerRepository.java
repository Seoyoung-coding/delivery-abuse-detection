package com.example.seller.repository;

import com.example.customer.domain.Customer;
import com.example.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository
        extends JpaRepository<Seller, Long> {


    // 1. 이 Customer가 이미 Seller인지 확인
    boolean existsByCustomer(Customer customer);


    // 2. Customer를 이용해서 Seller 찾기
    Optional<Seller> findByCustomer(Customer customer);
}