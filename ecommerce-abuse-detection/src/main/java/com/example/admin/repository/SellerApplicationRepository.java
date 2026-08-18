package com.example.admin.repository;

import com.example.customer.domain.Customer;
import com.example.seller.domain.SellerApplication;
import com.example.seller.enums.SellerApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerApplicationRepository
        extends JpaRepository<SellerApplication, Long> {


    // 특정 Customer가 특정 상태의 신청을 (pending 인지 등) 가지고 있는지 확인
    boolean existsByCustomerAndStatus(
            Customer customer,
            SellerApplicationStatus status
    );

    // Customer의 가장 최근 신청 조회
    Optional<SellerApplication>
    findTopByCustomerOrderByIdDesc(
            Customer customer
    );

    // 특정 상태의 신청 목록 조회
    List<SellerApplication>
    findByStatus(
            SellerApplicationStatus status
    );
}