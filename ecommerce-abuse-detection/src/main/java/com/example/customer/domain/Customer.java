package com.example.customer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    // =========================
    // Customer ID
    // DB 내부 고유 번호
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // Username
    // 사용자가 회원가입할 때 정하는 이름
    // =========================

    @Column(unique = true)
    private String username;


    // =========================
    // Email
    // =========================

    @Column(
            nullable = false,
            unique = true
    )
    private String email;


    // =========================
    // Password
    // =========================

    @Column(nullable = false)
    private String password;


    // =========================
    // 회원가입용 생성자
    // =========================

    public Customer(
            String username,
            String email,
            String password
    ) {

        this.username =
                username;

        this.email =
                email;

        this.password =
                password;
    }


    // =========================
    // 비밀번호 변경
    // =========================

    public void changePassword(
            String encodedPassword
    ) {

        this.password =
                encodedPassword;
    }


    // =========================
    // Soft Delete
    // =========================

    private boolean deleted =
            false;

    private LocalDateTime deletedAt;


    public void softDelete() {

        this.deleted =
                true;

        this.deletedAt =
                LocalDateTime.now();
    }
}