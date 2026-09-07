package com.example.chat.service;

import com.example.admin.domain.Admin;
import com.example.admin.enums.AdminRole;
import com.example.admin.repository.SellerApplicationRepository;
import com.example.admin.service.AdminAuthService;

import com.example.chat.domain.ChatMessage;
import com.example.chat.domain.ChatRoom;
import com.example.chat.enums.ChatClosedBy;
import com.example.chat.enums.ChatRoomStatus;
import com.example.chat.enums.MessageSender;
import com.example.chat.enums.SupportType;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatRoomRepository;

import com.example.customer.domain.Customer;
import com.example.customer.service.CustomerService;

import com.example.notification.service.NotificationService;

import com.example.seller.domain.Seller;
import com.example.seller.enums.SellerApplicationStatus;
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
    private final SellerApplicationRepository sellerApplicationRepository;

    private final CustomerService customerService;
    private final AdminAuthService adminAuthService;

    private final NotificationService notificationService;


    // =====================================================
    // Seller Support
    // Seller ID 검증 후 ACTIVE 방 조회 / 생성
    // =====================================================
    @Transactional
    public ChatRoom getOrCreateSellerSupportRoom(
            String authorizationHeader,
            Long inputSellerId
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        Seller seller =
                sellerRepository
                        .findByCustomer(customer)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "현재 계정은 Seller 계정이 아닙니다."
                                )
                        );


        if (
                !seller.getId()
                        .equals(inputSellerId)
        ) {

            throw new RuntimeException(
                    "Seller ID가 현재 로그인 계정과 일치하지 않습니다."
            );
        }


        return chatRoomRepository
                .findByCustomerAndSupportTypeAndStatus(
                        customer,
                        SupportType.SELLER_SUPPORT,
                        ChatRoomStatus.ACTIVE
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
    //
    // 신청 없음 / PENDING / REJECTED
    // → Customer Support 가능
    //
    // APPROVED
    // → Customer Support 불가
    // =====================================================
    @Transactional
    public ChatRoom getOrCreateCustomerSupportRoom(
            String authorizationHeader
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        boolean isApprovedSeller =
                sellerApplicationRepository
                        .findTopByCustomerOrderByIdDesc(
                                customer
                        )
                        .map(
                                application ->
                                        application.getStatus()
                                                == SellerApplicationStatus.APPROVED
                        )
                        .orElse(false);


        if (isApprovedSeller) {

            throw new RuntimeException(
                    "승인된 Seller 계정은 Seller Support를 이용해주세요."
            );
        }


        return chatRoomRepository
                .findByCustomerAndSupportTypeAndStatus(
                        customer,
                        SupportType.CUSTOMER_SUPPORT,
                        ChatRoomStatus.ACTIVE
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
    // Seller ID 검증 전용
    // ChatRoom 생성은 하지 않음
    // =====================================================
    @Transactional(readOnly = true)
    public boolean verifySellerIdentity(
            String authorizationHeader,
            Long inputSellerId
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        return sellerRepository
                .findByCustomer(customer)
                .map(
                        seller ->
                                seller.getId()
                                        .equals(inputSellerId)
                )
                .orElse(false);
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

        validateContent(content);


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

        validateContent(content);


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
    // Seller Support 메시지 조회
    //
    // 중요:
    // GET 요청은 새 ChatRoom을 만들지 않음
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getSellerSupportMessages(
            String authorizationHeader,
            Long sellerId
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        Seller seller =
                sellerRepository
                        .findByCustomer(customer)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "현재 계정은 Seller 계정이 아닙니다."
                                )
                        );


        if (
                !seller.getId()
                        .equals(sellerId)
        ) {

            throw new RuntimeException(
                    "Seller ID가 현재 로그인 계정과 일치하지 않습니다."
            );
        }


        return chatRoomRepository
                .findByCustomerAndSupportTypeAndStatus(
                        customer,
                        SupportType.SELLER_SUPPORT,
                        ChatRoomStatus.ACTIVE
                )
                .map(
                        chatMessageRepository
                                ::findByChatRoomOrderByCreatedAtAsc
                )
                .orElseGet(
                        List::of
                );
    }


    // =====================================================
    // Customer Support 메시지 조회
    //
    // 중요:
    // GET 요청은 새 ChatRoom을 만들지 않음
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getCustomerSupportMessages(
            String authorizationHeader
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        boolean isApprovedSeller =
                sellerApplicationRepository
                        .findTopByCustomerOrderByIdDesc(
                                customer
                        )
                        .map(
                                application ->
                                        application.getStatus()
                                                == SellerApplicationStatus.APPROVED
                        )
                        .orElse(false);


        if (isApprovedSeller) {

            throw new RuntimeException(
                    "승인된 Seller 계정은 Seller Support를 이용해주세요."
            );
        }


        return chatRoomRepository
                .findByCustomerAndSupportTypeAndStatus(
                        customer,
                        SupportType.CUSTOMER_SUPPORT,
                        ChatRoomStatus.ACTIVE
                )
                .map(
                        chatMessageRepository
                                ::findByChatRoomOrderByCreatedAtAsc
                )
                .orElseGet(
                        List::of
                );
    }


    // =====================================================
    // Admin : 본인 담당 ACTIVE 상담방 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatRoom> getAdminRooms(
            String authorizationHeader
    ) {

        Admin admin =
                adminAuthService.getCurrentAdmin(
                        authorizationHeader
                );


        SupportType supportType =
                getSupportTypeForAdmin(
                        admin
                );


        return chatRoomRepository
                .findBySupportTypeAndStatusOrderByUpdatedAtDesc(
                        supportType,
                        ChatRoomStatus.ACTIVE
                );
    }


    // =====================================================
    // Admin : 특정 상담방 메시지 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getAdminMessages(
            String authorizationHeader,
            Long roomId
    ) {

        Admin admin =
                adminAuthService.getCurrentAdmin(
                        authorizationHeader
                );


        ChatRoom room =
                getRoom(roomId);


        validateAdminRoomAccess(
                admin,
                room
        );


        return chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(
                        room
                );
    }


    // =====================================================
    // Admin : 메시지 전송
    // 실제 Admin Entity 저장
    // =====================================================
    @Transactional
    public ChatMessage sendAdminMessage(
            String authorizationHeader,
            Long roomId,
            String content
    ) {

        validateContent(content);


        Admin admin =
                adminAuthService.getCurrentAdmin(
                        authorizationHeader
                );


        ChatRoom room =
                getRoom(roomId);


        validateAdminRoomAccess(
                admin,
                room
        );


        if (room.isClosed()) {

            throw new RuntimeException(
                    "종료된 채팅방에는 메시지를 보낼 수 없습니다."
            );
        }


        ChatMessage message =
                new ChatMessage(
                        room,
                        admin,
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
    // Seller : Seller Support 종료
    // =====================================================
    @Transactional
    public void closeSellerSupportRoom(
            String authorizationHeader,
            Long sellerId
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        Seller seller =
                sellerRepository
                        .findByCustomer(customer)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "현재 계정은 Seller 계정이 아닙니다."
                                )
                        );


        if (
                !seller.getId()
                        .equals(sellerId)
        ) {

            throw new RuntimeException(
                    "Seller ID가 현재 계정과 일치하지 않습니다."
            );
        }


        ChatRoom room =
                chatRoomRepository
                        .findByCustomerAndSupportTypeAndStatus(
                                customer,
                                SupportType.SELLER_SUPPORT,
                                ChatRoomStatus.ACTIVE
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "진행 중인 Seller Support 채팅이 없습니다."
                                )
                        );


        room.close(
                ChatClosedBy.SELLER,
                "Seller가 상담을 종료했습니다."
        );


        notificationService
                .createSupportClosedNotification(
                        customer,
                        room.getId()
                );
    }


    // =====================================================
    // Customer : Customer Support 종료
    //
    // PENDING / REJECTED는 종료 가능
    // APPROVED Seller만 Customer Support 사용 불가
    // =====================================================
    @Transactional
    public void closeCustomerSupportRoom(
            String authorizationHeader
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        boolean isApprovedSeller =
                sellerApplicationRepository
                        .findTopByCustomerOrderByIdDesc(
                                customer
                        )
                        .map(
                                application ->
                                        application.getStatus()
                                                == SellerApplicationStatus.APPROVED
                        )
                        .orElse(false);


        if (isApprovedSeller) {

            throw new RuntimeException(
                    "승인된 Seller 계정은 Customer Support를 이용할 수 없습니다."
            );
        }


        ChatRoom room =
                chatRoomRepository
                        .findByCustomerAndSupportTypeAndStatus(
                                customer,
                                SupportType.CUSTOMER_SUPPORT,
                                ChatRoomStatus.ACTIVE
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "진행 중인 Customer Support 채팅이 없습니다."
                                )
                        );


        room.close(
                ChatClosedBy.CUSTOMER,
                "Customer가 상담을 종료했습니다."
        );


        notificationService
                .createSupportClosedNotification(
                        customer,
                        room.getId()
                );
    }


    // =====================================================
    // Admin : 상담 종료
    // =====================================================
    @Transactional
    public void closeAdminRoom(
            String authorizationHeader,
            Long roomId
    ) {

        Admin admin =
                adminAuthService.getCurrentAdmin(
                        authorizationHeader
                );


        ChatRoom room =
                getRoom(roomId);


        validateAdminRoomAccess(
                admin,
                room
        );


        if (room.isClosed()) {

            throw new RuntimeException(
                    "이미 종료된 채팅방입니다."
            );
        }


        room.close(
                ChatClosedBy.ADMIN,
                "상담원이 상담을 종료했습니다."
        );


        notificationService
                .createSupportClosedNotification(
                        room.getCustomer(),
                        room.getId()
                );
    }


    // =====================================================
    // Customer -> Seller 승인 시
    // 기존 Customer Support 자동 종료
    // =====================================================
    @Transactional
    public void autoCloseCustomerSupportOnSellerApproval(
            Customer customer
    ) {

        chatRoomRepository
                .findByCustomerAndSupportTypeAndStatus(
                        customer,
                        SupportType.CUSTOMER_SUPPORT,
                        ChatRoomStatus.ACTIVE
                )
                .ifPresent(
                        room -> {

                            room.close(
                                    ChatClosedBy.SYSTEM,
                                    "Seller 승인으로 Customer Support 상담이 자동 종료되었습니다."
                            );


                            notificationService
                                    .createSupportAutoClosedNotification(
                                            customer,
                                            room.getId()
                                    );
                        }
                );
    }


    // =====================================================
    // 종료된 Support Chat 조회
    // 본인의 기록만 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<ChatMessage> getClosedSupportMessages(
            String authorizationHeader,
            Long roomId
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        ChatRoom room =
                getRoom(roomId);


        if (
                !room.getCustomer()
                        .getId()
                        .equals(
                                customer.getId()
                        )
        ) {

            throw new RuntimeException(
                    "본인의 상담 기록만 조회할 수 있습니다."
            );
        }


        if (!room.isClosed()) {

            throw new RuntimeException(
                    "아직 종료되지 않은 상담입니다."
            );
        }


        return chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(
                        room
                );
    }


    // =====================================================
    // Admin Role -> Support Type
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
    // Admin 담당 상담방인지 검사
    // =====================================================
    private void validateAdminRoomAccess(
            Admin admin,
            ChatRoom room
    ) {

        SupportType allowed =
                getSupportTypeForAdmin(
                        admin
                );


        if (
                room.getSupportType()
                        != allowed
        ) {

            throw new RuntimeException(
                    "담당하지 않는 상담방에는 접근할 수 없습니다."
            );
        }
    }


    // =====================================================
    // ChatRoom 공통 조회
    // =====================================================
    private ChatRoom getRoom(
            Long roomId
    ) {

        return chatRoomRepository
                .findById(roomId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "채팅방을 찾을 수 없습니다."
                        )
                );
    }


    // =====================================================
    // 메시지 입력 검증
    // =====================================================
    private void validateContent(
            String content
    ) {

        if (
                content == null ||
                        content.isBlank()
        ) {

            throw new RuntimeException(
                    "메시지를 입력해주세요."
            );
        }
    }
}