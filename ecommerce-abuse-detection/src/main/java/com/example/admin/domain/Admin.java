package com.example.admin.domain;

import com.example.admin.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "admin")
public class Admin {

    // =========================
    // Admin 고유 ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // Admin 로그인 이메일
    // =========================

    @Column(
            nullable = false,
            unique = true
    )
    private String email;


    // =========================
    // Admin 로그인 비밀번호
    // =========================

    @Column(nullable = false)
    private String password;


    // =========================
    // Admin 담당 역할
    //
    // SELLER_ADMIN
    // CUSTOMER_ADMIN
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminRole role;


    // =========================
    // Admin 생성
    // =========================

    public Admin(
            String email,
            String password,
            AdminRole role
    ) {

        this.email = email;
        this.password = password;
        this.role = role;
    }
}