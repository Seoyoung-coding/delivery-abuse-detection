package com.example.order.repository;

import com.example.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomerId(Long customerId);

    // 고객의 전체 주문 수
    long countByCustomerId(Long customerId);
}