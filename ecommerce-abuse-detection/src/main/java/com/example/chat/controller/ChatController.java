package com.example.chat.controller;

import com.example.chat.domain.ChatMessage;
import com.example.chat.domain.ChatRoom;
import com.example.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    // =====================================================
    // Seller Support : 메시지 보내기
    //
    // POST /api/chat/support/seller/messages
    //
    // body:
    // {
    //   "sellerId": 3,
    //   "content": "상품 등록에 문제가 있습니다."
    // }
    // =====================================================

    @PostMapping("/support/seller/messages")
    public ResponseEntity<Map<String, Object>> sendSellerSupportMessage(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestBody
            Map<String, Object> request
    ) {

        Long sellerId =
                Long.valueOf(
                        request.get("sellerId")
                                .toString()
                );


        String content =
                request.get("content")
                        .toString();


        ChatMessage message =
                chatService
                        .sendSellerSupportMessage(
                                authorizationHeader,
                                sellerId,
                                content
                        );


        return ResponseEntity.ok(
                toResponse(message)
        );
    }


    // =====================================================
    // Seller Support : 메시지 전체 조회
    //
    // GET
    // /api/chat/support/seller/messages?sellerId=3
    // =====================================================

    @GetMapping("/support/seller/messages")
    public ResponseEntity<List<Map<String, Object>>> getSellerSupportMessages(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestParam
            Long sellerId
    ) {

        List<Map<String, Object>> messages =
                chatService
                        .getSellerSupportMessages(
                                authorizationHeader,
                                sellerId
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList();


        return ResponseEntity.ok(
                messages
        );
    }


    // =====================================================
    // Customer Support : 메시지 보내기
    //
    // POST /api/chat/support/customer/messages
    //
    // body:
    // {
    //   "content": "환불 문의입니다."
    // }
    // =====================================================

    @PostMapping("/support/customer/messages")
    public ResponseEntity<Map<String, Object>> sendCustomerSupportMessage(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestBody
            Map<String, String> request
    ) {

        String content =
                request.get("content");


        ChatMessage message =
                chatService
                        .sendCustomerSupportMessage(
                                authorizationHeader,
                                content
                        );


        return ResponseEntity.ok(
                toResponse(message)
        );
    }


    // =====================================================
    // Customer Support : 메시지 전체 조회
    //
    // GET /api/chat/support/customer/messages
    // =====================================================

    @GetMapping("/support/customer/messages")
    public ResponseEntity<List<Map<String, Object>>> getCustomerSupportMessages(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        List<Map<String, Object>> messages =
                chatService
                        .getCustomerSupportMessages(
                                authorizationHeader
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList();


        return ResponseEntity.ok(
                messages
        );
    }


    // =====================================================
    // Admin : 본인이 담당하는 Support 채팅방 전체 조회
    //
    // SELLER_ADMIN
    // -> SELLER_SUPPORT 방만 반환
    //
    // CUSTOMER_ADMIN
    // -> CUSTOMER_SUPPORT 방만 반환
    //
    // GET /api/chat/admin/rooms
    // =====================================================

    @GetMapping("/admin/rooms")
    public ResponseEntity<List<Map<String, Object>>> getAdminRooms(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        List<Map<String, Object>> rooms =
                chatService
                        .getAdminRooms(
                                authorizationHeader
                        )
                        .stream()
                        .map(this::roomToResponse)
                        .toList();


        return ResponseEntity.ok(
                rooms
        );
    }


    // =====================================================
    // Admin : 특정 채팅방 메시지 조회
    //
    // 본인이 담당하는 Support 방만 접근 가능
    //
    // GET /api/chat/admin/rooms/{roomId}/messages
    // =====================================================

    @GetMapping("/admin/rooms/{roomId}/messages")
    public ResponseEntity<List<Map<String, Object>>> getAdminMessages(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @PathVariable
            Long roomId
    ) {

        List<Map<String, Object>> messages =
                chatService
                        .getAdminMessages(
                                authorizationHeader,
                                roomId
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList();


        return ResponseEntity.ok(
                messages
        );
    }


    // =====================================================
    // Admin : 특정 채팅방에 답장
    //
    // 실제 로그인 Admin ID가 ChatMessage에 저장됨
    //
    // POST /api/chat/admin/rooms/{roomId}/messages
    // =====================================================

    @PostMapping("/admin/rooms/{roomId}/messages")
    public ResponseEntity<Map<String, Object>> sendAdminMessage(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @PathVariable
            Long roomId,

            @RequestBody
            Map<String, String> request
    ) {

        String content =
                request.get("content");


        ChatMessage message =
                chatService
                        .sendAdminMessage(
                                authorizationHeader,
                                roomId,
                                content
                        );


        return ResponseEntity.ok(
                toResponse(message)
        );
    }


    // =====================================================
    // ChatRoom -> Admin 화면 응답
    // =====================================================

    private Map<String, Object> roomToResponse(
            ChatRoom room
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "roomId",
                room.getId()
        );

        response.put(
                "customerId",
                room.getCustomer().getId()
        );

        response.put(
                "customerEmail",
                room.getCustomer().getEmail()
        );

        response.put(
                "supportType",
                room.getSupportType()
        );

        response.put(
                "createdAt",
                room.getCreatedAt()
        );

        response.put(
                "updatedAt",
                room.getUpdatedAt()
        );


        return response;
    }


    // =====================================================
    // ChatMessage -> Frontend 응답
    // =====================================================

    private Map<String, Object> toResponse(
            ChatMessage message
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "id",
                message.getId()
        );

        response.put(
                "sender",
                message.getSender()
        );

        response.put(
                "content",
                message.getContent()
        );

        response.put(
                "createdAt",
                message.getCreatedAt()
        );


        return response;
    }

    // =====================================================
    // Seller Support : Seller ID 검증
    //
    // GET /api/chat/support/seller/verify?sellerId=3
    // =====================================================
    @GetMapping("/support/seller/verify")
    public ResponseEntity<Map<String, Object>> verifySeller(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestParam
            Long sellerId
    ) {

        boolean verified =
                chatService.verifySellerIdentity(
                        authorizationHeader,
                        sellerId
                );


        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "verified",
                verified
        );


        if (!verified) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }


        response.put(
                "sellerId",
                sellerId
        );


        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
// Seller : Seller Support 종료
//
// PATCH /api/chat/support/seller/close?sellerId=3
// =====================================================
    @PatchMapping("/support/seller/close")
    public ResponseEntity<String> closeSellerSupport(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestParam
            Long sellerId
    ) {

        chatService.closeSellerSupportRoom(
                authorizationHeader,
                sellerId
        );

        return ResponseEntity.ok(
                "Seller Support chat closed"
        );
    }


    // =====================================================
    // Customer : Customer Support 종료
    //
    // PATCH /api/chat/support/customer/close
    // =====================================================
    @PatchMapping("/support/customer/close")
    public ResponseEntity<String> closeCustomerSupport(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        chatService.closeCustomerSupportRoom(
                authorizationHeader
        );

        return ResponseEntity.ok(
                "Customer Support chat closed"
        );
    }


    // =====================================================
    // Admin : 담당 채팅 종료
    //
    // PATCH /api/chat/admin/rooms/{roomId}/close
    // =====================================================
    @PatchMapping("/admin/rooms/{roomId}/close")
    public ResponseEntity<String> closeAdminChat(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @PathVariable
            Long roomId
    ) {

        chatService.closeAdminRoom(
                authorizationHeader,
                roomId
        );

        return ResponseEntity.ok(
                "Support chat closed"
        );
    }
}