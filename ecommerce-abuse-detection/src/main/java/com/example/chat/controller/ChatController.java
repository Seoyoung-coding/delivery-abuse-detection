package com.example.chat.controller;

import com.example.chat.domain.ChatMessage;
import com.example.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

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
    // Seller : 메시지 보내기
    // POST /api/chat/seller/messages
    // =====================================================
    @PostMapping("/seller/messages")
    public ResponseEntity<Map<String, Object>> sendSellerMessage(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestBody
            Map<String, String> request
    ) {

        // 프론트에서 보낸 content 가져오기
        String content =
                request.get("content");


        // DB에 메시지 저장
        ChatMessage message =
                chatService.sendSellerMessage(
                        authorizationHeader,
                        content
                );


        return ResponseEntity.ok(
                toResponse(message)
        );
    }


    // =====================================================
    // Seller : 자기 채팅 전체 조회
    // GET /api/chat/seller/messages
    // =====================================================
    @GetMapping("/seller/messages")
    public ResponseEntity<List<Map<String, Object>>> getSellerMessages(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        List<Map<String, Object>> messages =
                chatService
                        .getSellerMessages(
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
    // ChatMessage -> 프론트에 보낼 데이터
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
// Admin : 전체 Seller 채팅방 조회
// GET /api/chat/admin/rooms
// =====================================================
    @GetMapping("/admin/rooms")
    public ResponseEntity<List<Map<String, Object>>> getAdminRooms() {

        List<Map<String, Object>> rooms =
                chatService
                        .getAdminRooms()
                        .stream()
                        .map(room -> {

                            Map<String, Object> response =
                                    new LinkedHashMap<>();

                            response.put(
                                    "roomId",
                                    room.getId()
                            );

                            response.put(
                                    "sellerId",
                                    room.getSeller().getId()
                            );

                            response.put(
                                    "sellerEmail",
                                    room.getSeller()
                                            .getCustomer()
                                            .getEmail()
                            );

                            response.put(
                                    "createdAt",
                                    room.getCreatedAt()
                            );

                            return response;
                        })
                        .toList();


        return ResponseEntity.ok(
                rooms
        );
    }

    // =====================================================
// Admin : 특정 Seller 채팅방 메시지 전체 조회
// GET /api/chat/admin/rooms/{roomId}/messages
// =====================================================
    @GetMapping("/admin/rooms/{roomId}/messages")
    public ResponseEntity<List<Map<String, Object>>> getAdminMessages(
            @PathVariable Long roomId
    ) {

        List<Map<String, Object>> messages =
                chatService
                        .getAdminMessages(roomId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(
                messages
        );
    }


    // =====================================================
    // Admin : 특정 Seller 채팅방에 답장
    // POST /api/chat/admin/rooms/{roomId}/messages
    // =====================================================
    @PostMapping("/admin/rooms/{roomId}/messages")
    public ResponseEntity<Map<String, Object>> sendAdminMessage(

            @PathVariable Long roomId,

            @RequestBody
            Map<String, String> request
    ) {

        String content =
                request.get("content");

        ChatMessage message =
                chatService.sendAdminMessage(
                        roomId,
                        content
                );

        return ResponseEntity.ok(
                toResponse(message)
        );
    }
}