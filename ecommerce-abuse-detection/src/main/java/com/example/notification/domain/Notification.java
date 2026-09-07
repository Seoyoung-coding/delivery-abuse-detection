package com.example.notification.domain;

import com.example.customer.domain.Customer;
import com.example.notification.enums.NotificationType;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class Notification {

    // =========================
    // Notification ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // 알림을 받을 Customer
    // Seller도 Customer 기반 계정이므로
    // Customer로 통일
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;


    // =========================
    // 알림 종류
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;


    // =========================
    // 제목
    // =========================

    @Column(nullable = false)
    private String title;


    // =========================
    // 내용
    // =========================

    @Column(
            nullable = false,
            length = 1000
    )
    private String message;


    // =========================
    // 관련 ChatRoom
    //
    // 채팅 알림이 아니면 null 가능
    // =========================

    private Long chatRoomId;


    // =========================
    // 관련 Order
    //
    // 나중에 Receipt 연결
    // =========================

    private Long orderId;


    // =========================
    // 읽음 여부
    // =========================

    private boolean read =
            false;


    // =========================
    // 생성 시간
    // =========================

    private LocalDateTime createdAt;


    // =========================
    // 생성자
    // =========================

    public Notification(
            Customer customer,
            NotificationType type,
            String title,
            String message,
            Long chatRoomId,
            Long orderId
    ) {

        this.customer =
                customer;

        this.type =
                type;

        this.title =
                title;

        this.message =
                message;

        this.chatRoomId =
                chatRoomId;

        this.orderId =
                orderId;

        this.createdAt =
                LocalDateTime.now();
    }


    // =========================
    // 읽음 처리
    // =========================

    public void markAsRead() {

        this.read =
                true;
    }
}