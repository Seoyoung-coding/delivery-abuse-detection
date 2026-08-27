package com.example.chat.service;

import com.example.chat.domain.ChatMessage;
import com.example.chat.domain.ChatRoom;
import com.example.chat.enums.MessageSender;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.customer.domain.Customer;
import com.example.customer.service.CustomerService;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SellerRepository sellerRepository;
    private final CustomerService customerService;


    // =====================================================
    // Seller : 자기 채팅방 가져오기
    // 채팅방이 없으면 새로 생성
    // =====================================================
    @Transactional
    public ChatRoom getOrCreateSellerRoom(
            String authorizationHeader
    ) {

        // 1. 현재 로그인한 Customer 가져오기
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 2. 현재 Customer가 Seller인지 확인
        Seller seller =
                sellerRepository
                        .findByCustomer(customer)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Seller가 아닙니다."
                                )
                        );


        // 3. 기존 채팅방이 있으면 반환
        // 4. 없으면 Seller와 연결된 새 채팅방 생성
        return chatRoomRepository
                .findBySeller(seller)
                .orElseGet(() -> {

                    ChatRoom chatRoom =
                            new ChatRoom(seller);

                    return chatRoomRepository.save(
                            chatRoom
                    );
                });
    }


    // =====================================================
    // Seller : 메시지 보내기
    // =====================================================
    @Transactional
    public ChatMessage sendSellerMessage(
            String authorizationHeader,
            String content
    ) {

        // 1. Seller의 채팅방 가져오기
        ChatRoom chatRoom =
                getOrCreateSellerRoom(
                        authorizationHeader
                );


        // 2. 빈 메시지 방지
        if (
                content == null ||
                        content.isBlank()
        ) {
            throw new RuntimeException(
                    "메시지를 입력해주세요."
            );
        }


        // 3. 새로운 메시지 생성
        ChatMessage message =
                new ChatMessage(
                        chatRoom,
                        MessageSender.SELLER,
                        content
                );


        // 4. DB 저장
        return chatMessageRepository.save(
                message
        );
    }


    // =====================================================
    // Seller : 자기 채팅 메시지 전체 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getSellerMessages(
            String authorizationHeader
    ) {

        // 1. Seller의 채팅방 가져오기
        ChatRoom chatRoom =
                getOrCreateSellerRoom(
                        authorizationHeader
                );


        // 2. 채팅방의 메시지를 시간순으로 조회
        return chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(
                        chatRoom
                );
    }

    // admin

    @Transactional(readOnly = true)
    public List<ChatRoom> getAdminRooms() {

        return chatRoomRepository
                .findAllByOrderByCreatedAtDesc();
    }

}