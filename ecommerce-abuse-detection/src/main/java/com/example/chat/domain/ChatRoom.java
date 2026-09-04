package com.example.chat.domain;

import com.example.seller.domain.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom { // "이 Seller가 Admin과 대화하는 방

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // seller 한명당 1개의 채팅방
    @JoinColumn(
            name = "seller_id",
            nullable = false,
            unique = true
    )
    private Seller seller;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public ChatRoom(Seller seller) {
        this.seller = seller;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}