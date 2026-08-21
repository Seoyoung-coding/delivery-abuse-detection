package com.example.customer.domain;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // db의 customers 테이블과 연결
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 아이디를 자동 생성하게 함
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public Customer(String email, String password) { // 새로운 객체를 만드는 생성자
        this.email = email;
        this.password = password;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    private boolean deleted = false;
    private LocalDateTime deletedAt;

    // Soft Delete
    public void softDelete() {

        // 탈퇴 상태로 변경
        this.deleted = true;

        // 탈퇴 시간 기록
        this.deletedAt = LocalDateTime.now();
    }
}