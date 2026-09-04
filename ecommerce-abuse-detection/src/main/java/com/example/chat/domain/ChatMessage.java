package com.example.chat.domain;

import com.example.chat.enums.MessageSender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "chat_room_id",
            nullable = false
    )
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    private MessageSender sender;

    @Column(nullable = false, length = 2000)
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    public void markAsRead() {
        this.readAt = LocalDateTime.now();
    }
    public ChatMessage(
            ChatRoom chatRoom,
            MessageSender sender,
            String content
    ) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}