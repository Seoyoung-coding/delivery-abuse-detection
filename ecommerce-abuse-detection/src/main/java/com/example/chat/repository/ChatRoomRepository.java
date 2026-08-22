package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import com.example.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findBySeller(Seller seller);
}