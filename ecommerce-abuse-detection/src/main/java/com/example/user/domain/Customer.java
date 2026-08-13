package com.example.user.domain;
import jakarta.persistence.*;
import lombok.*;

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
}