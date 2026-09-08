package com.example.refund.repository;

import com.example.refund.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    // 특정 고객의 전체 환불 수
    @Query("""
            SELECT COUNT(r)
            FROM Refund r
            WHERE r.order.customer.id = :customerId
            """)
    long countByCustomerId(
            @Param("customerId") Long customerId
    );
}