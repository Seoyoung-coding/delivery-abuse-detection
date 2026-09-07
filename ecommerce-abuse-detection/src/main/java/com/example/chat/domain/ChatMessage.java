package com.example.chat.domain;

import com.example.admin.domain.Admin;
import com.example.chat.enums.MessageSender;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    // =========================
    // Message ID
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
    // 어느 ChatRoom의 메시지인지
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "chat_room_id",
            nullable = false
    )
    private ChatRoom chatRoom;


    // =========================
    // 누가 보낸 메시지인지
    //
    // SELLER
    // CUSTOMER
    // ADMIN
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageSender sender;


    // =========================
    // 실제 Admin 계정
    //
    // Seller / Customer가 보낸 메시지:
    // admin = null
    //
    // Admin이 보낸 메시지:
    // admin = 실제 Admin Entity
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "admin_id"
    )
    private Admin admin;


    // =========================
    // 메시지 내용
    // =========================

    @Column(
            nullable = false,
            length = 2000
    )
    private String content;


    // =========================
    // 시간
    // =========================

    private LocalDateTime createdAt;

    private LocalDateTime readAt;


    // =====================================================
    // Seller / Customer가 메시지를 보낼 때
    // =====================================================

    public ChatMessage(
            ChatRoom chatRoom,
            MessageSender sender,
            String content
    ) {

        this.chatRoom =
                chatRoom;

        this.sender =
                sender;

        // 일반 사용자가 보냈으므로 Admin 없음
        this.admin =
                null;

        this.content =
                content;

        this.createdAt =
                LocalDateTime.now();
    }


    // =====================================================
    // Admin이 메시지를 보낼 때
    // =====================================================

    public ChatMessage(
            ChatRoom chatRoom,
            Admin admin,
            String content
    ) {

        this.chatRoom =
                chatRoom;

        this.sender =
                MessageSender.ADMIN;

        // 실제 답장한 Admin 계정 저장
        this.admin =
                admin;

        this.content =
                content;

        this.createdAt =
                LocalDateTime.now();
    }


    // =========================
    // 읽음 처리
    // =========================

    public void markAsRead() {

        this.readAt =
                LocalDateTime.now();
    }
}