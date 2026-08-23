package com.example.chat.service;

import com.example.chat.domain.ChatRoom;
import com.example.chat.repository.ChatRoomRepository;
import com.example.customer.domain.Customer;
import com.example.customer.service.CustomerService;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
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

        // 1. JWT를 이용해서 현재 로그인한 Customer 가져오기
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 2. 현재 Customer가 Seller인지 확인
        Seller seller =
                sellerRepository.findByCustomer(customer)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Seller가 아닙니다."
                                )
                        );


        // 3. 이미 Seller의 채팅방이 존재하면 반환
        return chatRoomRepository
                .findBySeller(seller)

                // 4. 없으면 새로운 채팅방 생성
                .orElseGet(() -> {

                    ChatRoom chatRoom =
                            new ChatRoom();

                    return chatRoomRepository.save(
                            chatRoom
                    );
                });
    }
}