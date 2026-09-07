package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import com.example.chat.enums.SupportType;
import com.example.customer.domain.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {


    // =====================================================
    // 사용자 + 상담 종류로 채팅방 조회
    //
    // 예:
    // Customer 7 + SELLER_SUPPORT
    // Customer 7 + CUSTOMER_SUPPORT
    // =====================================================

    Optional<ChatRoom> findByCustomerAndSupportType(
            Customer customer,
            SupportType supportType
    );


    // =====================================================
    // Admin : 담당 상담 종류의 채팅방 전체 조회
    //
    // SELLER_ADMIN
    // -> SELLER_SUPPORT만 조회
    //
    // CUSTOMER_ADMIN
    // -> CUSTOMER_SUPPORT만 조회
    // =====================================================

    List<ChatRoom> findBySupportTypeOrderByUpdatedAtDesc(
            SupportType supportType
    );
}