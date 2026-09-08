package com.example.abuse.repository;

import com.example.abuse.domain.AbuseCase;
import com.example.abuse.enums.AbuseCaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbuseCaseRepository extends JpaRepository<AbuseCase, Long> {

    // 특정 상태의 케이스 조회
    List<AbuseCase> findByStatus(AbuseCaseStatus status);

    // 특정 고객의 Abuse Case 기록
    List<AbuseCase> findByCustomerId(Long customerId);
}