package com.example.customer.repository;

import com.example.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    // =========================
    // Email
    // =========================

    Optional<Customer> findByEmail(
            String email
    );

    boolean existsByEmail(
            String email
    );


    // =========================
    // Username
    // =========================

    boolean existsByUsername(
            String username
    );

    Optional<Customer> findByUsername(
            String username
    );


    // =========================
    // 탈퇴하지 않은 회원 조회
    // =========================

    Optional<Customer> findByEmailAndDeletedFalse(
            String email
    );
}