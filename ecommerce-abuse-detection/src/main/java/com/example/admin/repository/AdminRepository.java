package com.example.admin.repository;

import com.example.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository
        extends JpaRepository<Admin, Long> { // 이메일로 어드민 계정을 찾을 수 있게함

    // =========================
    // Admin 로그인용 이메일 조회
    // =========================

    Optional<Admin> findByEmail(
            String email
    );
}