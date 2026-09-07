package com.example.chat.repository;

import com.example.chat.domain.ChatRoom;
import com.example.chat.enums.ChatRoomStatus;
import com.example.chat.enums.SupportType;
import com.example.customer.domain.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {


    // =====================================================
    // 기존 메서드
    // 다음 단계에서 Service 수정 후 삭제 예정
    // =====================================================

    Optional<ChatRoom> findByCustomerAndSupportType(
            Customer customer,
            SupportType supportType
    );


    List<ChatRoom> findBySupportTypeOrderByUpdatedAtDesc(
            SupportType supportType
    );


    // =====================================================
    // 현재 ACTIVE 상태인 상담방 조회
    //
    // 사용자 한 명이 같은 SupportType에서
    // 현재 진행 중인 방을 찾을 때 사용
    // =====================================================

    Optional<ChatRoom> findByCustomerAndSupportTypeAndStatus(
            Customer customer,
            SupportType supportType,
            ChatRoomStatus status
    );


    // =====================================================
    // Admin : 본인 담당 ACTIVE 상담방만 조회
    // =====================================================

    List<ChatRoom> findBySupportTypeAndStatusOrderByUpdatedAtDesc(
            SupportType supportType,
            ChatRoomStatus status
    );
}