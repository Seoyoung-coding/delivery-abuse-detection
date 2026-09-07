package com.example.notification.controller;

import com.example.notification.domain.Notification;
import com.example.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    // =====================================================
    // 현재 로그인 사용자의 알림 전체 조회
    //
    // GET /api/notifications
    // =====================================================

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getMyNotifications(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        List<Map<String, Object>> notifications =
                notificationService
                        .getMyNotifications(
                                authorizationHeader
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList();


        return ResponseEntity.ok(
                notifications
        );
    }


    // =====================================================
    // 읽지 않은 알림 개수
    //
    // GET /api/notifications/unread-count
    // =====================================================

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        long count =
                notificationService
                        .getUnreadCount(
                                authorizationHeader
                        );


        Map<String, Long> response =
                new LinkedHashMap<>();


        response.put(
                "count",
                count
        );


        return ResponseEntity.ok(
                response
        );
    }


    // =====================================================
    // 알림 읽음 처리
    //
    // PATCH /api/notifications/{notificationId}/read
    // =====================================================

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @PathVariable
            Long notificationId
    ) {

        notificationService
                .markAsRead(
                        authorizationHeader,
                        notificationId
                );


        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }


    // =====================================================
    // Notification -> Frontend 응답
    // =====================================================

    private Map<String, Object> toResponse(
            Notification notification
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "id",
                notification.getId()
        );

        response.put(
                "type",
                notification.getType()
        );

        response.put(
                "title",
                notification.getTitle()
        );

        response.put(
                "message",
                notification.getMessage()
        );

        response.put(
                "chatRoomId",
                notification.getChatRoomId()
        );

        response.put(
                "orderId",
                notification.getOrderId()
        );

        response.put(
                "read",
                notification.isRead()
        );

        response.put(
                "createdAt",
                notification.getCreatedAt()
        );


        return response;
    }
}