package com.example.chat.domain;

import com.example.chat.enums.SupportType;
import com.example.customer.domain.Customer;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor

@Table(
        name = "chat_room",

        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "customer_id",
                                "support_type"
                        }
                )
        }
)
public class ChatRoom {

    // =========================
    // ChatRoom ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // 상담을 요청한 사용자
    // =========================
    //
    // Customer든 Seller든
    // 기본 계정은 Customer를 기준으로 식별
    //
    // Seller도 Customer와 연결되어 있으므로
    // Seller 상담 역시 Customer ID를 사용
    //

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;


    // =========================
    // 상담 종류
    // =========================
    //
    // SELLER_SUPPORT
    // → Seller 담당 Admin
    //
    // CUSTOMER_SUPPORT
    // → Customer 담당 Admin
    //

    @Enumerated(EnumType.STRING)
    @Column(
            name = "support_type",
            nullable = false
    )
    private SupportType supportType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // =========================
    // 채팅방 생성
    // =========================

    public ChatRoom(
            Customer customer,
            SupportType supportType
    ) {

        this.customer = customer;
        this.supportType = supportType;

        this.createdAt =
                LocalDateTime.now();

        this.updatedAt =
                LocalDateTime.now();
    }


    public void updateTimestamp() {
        this.updatedAt =
                LocalDateTime.now();
    }
}