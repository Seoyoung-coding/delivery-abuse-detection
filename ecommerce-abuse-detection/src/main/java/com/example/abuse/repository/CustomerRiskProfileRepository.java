package com.example.abuse.repository;

import com.example.abuse.domain.CustomerRiskProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;


// 현재 db의 데이터에서 새로운 주문이 하나 생기면 db가 반영하도록 함
public interface CustomerRiskProfileRepository
        extends JpaRepository<CustomerRiskProfile, Long> {

    @Modifying
    @Query("""
        UPDATE CustomerRiskProfile p
        SET p.totalOrderCount = p.totalOrderCount + 1,
            p.totalOrderAmount = p.totalOrderAmount + :amount,
            p.updatedAt = CURRENT_TIMESTAMP
        WHERE p.customerId = :customerId
    """)
    int incrementOrder(
            @Param("customerId") Long customerId,
            @Param("amount") BigDecimal amount
    );
}