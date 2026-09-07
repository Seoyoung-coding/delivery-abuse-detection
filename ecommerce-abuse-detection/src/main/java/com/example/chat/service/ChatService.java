package com.example.chat.service;

import com.example.admin.domain.Admin;
import com.example.admin.enums.AdminRole;
import com.example.admin.service.AdminAuthService;
import com.example.chat.domain.ChatMessage;
import com.example.chat.domain.ChatRoom;
import com.example.chat.enums.MessageSender;
import com.example.chat.enums.SupportType;
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
    private final AdminAuthService adminAuthService;


    // =====================================================
    // Seller Support
    // Seller ID 검증 후 채팅방 가져오기 / 생성
    // =====================================================
    @Transactional
    public ChatRoom getOrCreateSellerSupportRoom(
            String authorizationHeader,
            Long inputSellerId
    ) {

        // 1. JWT 기준 현재 로그인 Customer
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 2. 현재 Customer와 연결된 Seller 조회
        //
        // 일반 Customer라면 여기서 실패
        Seller seller =
                sellerRepository
                        .findByCustomer(customer)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "현재 계정은 Seller 계정이 아닙니다."
                                )
                        );


        // 3. 사용자가 입력한 Seller ID와
        // 실제 로그인 계정의 Seller ID 비교
        if (
                !seller.getId()
                        .equals(inputSellerId)
        ) {

            throw new RuntimeException(
                    "Seller ID가 현재 로그인 계정과 일치하지 않습니다."
            );
        }


        // 4. 검증 성공
        // Seller Support 채팅방 조회
        // 없으면 새로 생성
        return chatRoomRepository
                .findByCustomerAndSupportType(
                        customer,
                        SupportType.SELLER_SUPPORT
                )
                .orElseGet(() -> {

                    ChatRoom room =
                            new ChatRoom(
                                    customer,
                                    SupportType.SELLER_SUPPORT
                            );

                    return chatRoomRepository.save(
                            room
                    );
                });
    }


    // =====================================================
// Customer Support
// 일반 Customer만 채팅방 가져오기 / 생성
//
// Seller 계정은 Customer Support 사용 불가
// =====================================================
    @Transactional
    public ChatRoom getOrCreateCustomerSupportRoom(
            String authorizationHeader
    ) {

        // 1. JWT 기준 현재 로그인 Customer
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 2. 현재 Customer가 Seller인지 확인
        //
        // Seller와 연결되어 있다면
        // Customer Support 사용 불가
        boolean isSeller =
                sellerRepository
                        .findByCustomer(customer)
                        .isPresent();


        if (isSeller) {

            throw new RuntimeException(
                    "Seller 계정은 Customer Support를 이용할 수 없습니다."
            );
        }


        // 3. Customer Support 채팅방 조회
        // 없으면 새로 생성
        return chatRoomRepository
                .findByCustomerAndSupportType(
                        customer,
                        SupportType.CUSTOMER_SUPPORT
                )
                .orElseGet(() -> {

                    ChatRoom room =
                            new ChatRoom(
                                    customer,
                                    SupportType.CUSTOMER_SUPPORT
                            );

                    return chatRoomRepository.save(
                            room
                    );
                });
    }

    // =====================================================
    // Seller -> Seller Support 메시지 전송
    // =====================================================
    @Transactional
    public ChatMessage sendSellerSupportMessage(
            String authorizationHeader,
            Long sellerId,
            String content
    ) {

        if (
                content == null
                        ||
                        content.isBlank()
        ) {

            throw new RuntimeException(
                    "메시지를 입력해주세요."
            );
        }


        // Seller ID 검증까지 포함
        ChatRoom room =
                getOrCreateSellerSupportRoom(
                        authorizationHeader,
                        sellerId
                );


        ChatMessage message =
                new ChatMessage(
                        room,
                        MessageSender.SELLER,
                        content
                );


        ChatMessage savedMessage =
                chatMessageRepository.save(
                        message
                );


        // 최근 상담 순서 갱신
        room.updateTimestamp();


        return savedMessage;
    }


    // =====================================================
    // Customer -> Customer Support 메시지 전송
    // =====================================================
    @Transactional
    public ChatMessage sendCustomerSupportMessage(
            String authorizationHeader,
            String content
    ) {

        if (
                content == null
                        ||
                        content.isBlank()
        ) {

            throw new RuntimeException(
                    "메시지를 입력해주세요."
            );
        }


        ChatRoom room =
                getOrCreateCustomerSupportRoom(
                        authorizationHeader
                );


        ChatMessage message =
                new ChatMessage(
                        room,
                        MessageSender.CUSTOMER,
                        content
                );


        ChatMessage savedMessage =
                chatMessageRepository.save(
                        message
                );


        room.updateTimestamp();


        return savedMessage;
    }


    // =====================================================
    // Seller : Seller Support 메시지 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getSellerSupportMessages(
            String authorizationHeader,
            Long sellerId
    ) {

        ChatRoom room =
                getOrCreateSellerSupportRoom(
                        authorizationHeader,
                        sellerId
                );


        return chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(
                        room
                );
    }


    // =====================================================
    // Customer : Customer Support 메시지 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getCustomerSupportMessages(
            String authorizationHeader
    ) {

        ChatRoom room =
                getOrCreateCustomerSupportRoom(
                        authorizationHeader
                );


        return chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(
                        room
                );
    }


    // =====================================================
    // Admin : 본인 담당 Support 채팅방 전체 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatRoom> getAdminRooms(
            String authorizationHeader
    ) {

        // 1. 현재 로그인한 실제 Admin 조회
        Admin admin =
                adminAuthService
                        .getCurrentAdmin(
                                authorizationHeader
                        );


        // 2. Admin 역할에 맞는 Support 종류 결정
        SupportType supportType =
                getSupportTypeForAdmin(
                        admin
                );


        // 3. 본인이 담당하는 상담방만 조회
        return chatRoomRepository
                .findBySupportTypeOrderByUpdatedAtDesc(
                        supportType
                );
    }


    // =====================================================
    // Admin : 특정 채팅방 메시지 조회
    // 본인 담당 Support 방만 조회 가능
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getAdminMessages(
            String authorizationHeader,
            Long roomId
    ) {

        // 1. 실제 로그인 Admin
        Admin admin =
                adminAuthService
                        .getCurrentAdmin(
                                authorizationHeader
                        );


        // 2. 채팅방 조회
        ChatRoom room =
                chatRoomRepository
                        .findById(roomId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "채팅방을 찾을 수 없습니다."
                                )
                        );


        // 3. Admin 담당 Support와
        // 채팅방 Support가 일치하는지 검사
        validateAdminRoomAccess(
                admin,
                room
        );


        // 4. 메시지 조회
        return chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(
                        room
                );
    }


    // =====================================================
    // Admin : 특정 채팅방에 답장
    // 실제 Admin ID까지 ChatMessage에 저장
    // =====================================================
    @Transactional
    public ChatMessage sendAdminMessage(
            String authorizationHeader,
            Long roomId,
            String content
    ) {

        // 1. 빈 메시지 방지
        if (
                content == null
                        ||
                        content.isBlank()
        ) {

            throw new RuntimeException(
                    "메시지를 입력해주세요."
            );
        }


        // 2. 실제 로그인 Admin 조회
        Admin admin =
                adminAuthService
                        .getCurrentAdmin(
                                authorizationHeader
                        );


        // 3. 채팅방 조회
        ChatRoom room =
                chatRoomRepository
                        .findById(roomId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "채팅방을 찾을 수 없습니다."
                                )
                        );


        // 4. 본인 담당 Support 방인지 검사
        validateAdminRoomAccess(
                admin,
                room
        );


        // 5. 실제 Admin Entity를 포함한 메시지 생성
        ChatMessage message =
                new ChatMessage(
                        room,
                        admin,
                        content
                );


        // 6. DB 저장
        ChatMessage savedMessage =
                chatMessageRepository.save(
                        message
                );


        // 7. 최근 활동 시간 갱신
        room.updateTimestamp();


        return savedMessage;
    }


    // =====================================================
    // Admin Role -> 담당 Support Type
    // =====================================================
    private SupportType getSupportTypeForAdmin(
            Admin admin
    ) {

        if (
                admin.getRole()
                        == AdminRole.SELLER_ADMIN
        ) {

            return SupportType.SELLER_SUPPORT;
        }


        if (
                admin.getRole()
                        == AdminRole.CUSTOMER_ADMIN
        ) {

            return SupportType.CUSTOMER_SUPPORT;
        }


        throw new RuntimeException(
                "지원하지 않는 Admin role입니다."
        );
    }


    // =====================================================
    // Admin이 해당 채팅방에 접근 가능한지 검사
    // =====================================================
    private void validateAdminRoomAccess(
            Admin admin,
            ChatRoom room
    ) {

        SupportType allowedSupportType =
                getSupportTypeForAdmin(
                        admin
                );


        if (
                room.getSupportType()
                        != allowedSupportType
        ) {

            throw new RuntimeException(
                    "담당하지 않는 상담방에는 접근할 수 없습니다."
            );
        }
    }
}