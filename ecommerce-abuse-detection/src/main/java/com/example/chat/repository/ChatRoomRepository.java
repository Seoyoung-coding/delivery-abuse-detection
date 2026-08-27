package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import com.example.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    // Seller 자신의 채팅방 조회
    Optional<ChatRoom> findBySeller(Seller seller);

    // Admin이 전체 Seller 채팅방 조회
    List<ChatRoom> findAllByOrderByCreatedAtDesc(); // 최근 생성된 채팅부터 보여줌

}
