package com.example.chat.domain;

import com.example.chat.enums.ChatClosedBy;
import com.example.chat.enums.ChatRoomStatus;
import com.example.chat.enums.SupportType;
import com.example.customer.domain.Customer;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_room")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;


    // =========================
    // 상담 종류
    //
    // SELLER_SUPPORT
    // CUSTOMER_SUPPORT
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "support_type",
            nullable = false
    )
    private SupportType supportType;


    // =========================
    // 채팅방 상태
    //
    // ACTIVE
    // CLOSED
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChatRoomStatus status =
            ChatRoomStatus.ACTIVE;


    // =========================
    // 누가 종료했는지
    //
    // CUSTOMER
    // SELLER
    // ADMIN
    // SYSTEM
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "closed_by")
    private ChatClosedBy closedBy;


    // =========================
    // 종료 시간
    // =========================

    private LocalDateTime closedAt;


    // =========================
    // 종료 사유
    // =========================

    @Column(length = 500)
    private String closeReason;


    // =========================
    // 생성 / 최근 활동 시간
    // =========================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // =========================
    // ChatRoom 생성
    // =========================

    public ChatRoom(
            Customer customer,
            SupportType supportType
    ) {

        this.customer =
                customer;

        this.supportType =
                supportType;

        this.status =
                ChatRoomStatus.ACTIVE;

        this.createdAt =
                LocalDateTime.now();

        this.updatedAt =
                LocalDateTime.now();
    }


    // =========================
    // 최근 메시지 시간 갱신
    // =========================

    public void updateTimestamp() {

        this.updatedAt =
                LocalDateTime.now();
    }


    // =========================
    // 채팅방 종료
    // =========================

    public void close(
            ChatClosedBy closedBy,
            String reason
    ) {

        this.status =
                ChatRoomStatus.CLOSED;

        this.closedBy =
                closedBy;

        this.closeReason =
                reason;

        this.closedAt =
                LocalDateTime.now();

        this.updatedAt =
                LocalDateTime.now();
    }


    // =========================
    // 종료 여부
    // =========================

    public boolean isClosed() {

        return this.status ==
                ChatRoomStatus.CLOSED;
    }
}