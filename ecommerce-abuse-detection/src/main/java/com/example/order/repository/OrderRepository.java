package com.example.order.repository;

import com.example.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    // 특정 고객의 주문 목록 조회
    List<OrderEntity> findByCustomerId(Long customerId);
}