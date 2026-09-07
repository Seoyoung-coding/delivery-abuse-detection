package com.example.notification.service;

import com.example.customer.domain.Customer;
import com.example.customer.service.CustomerService;
import com.example.notification.domain.Notification;
import com.example.notification.enums.NotificationType;
import com.example.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final CustomerService customerService;


    // =====================================================
    // 현재 로그인 Customer의 알림 전체 조회
    // 최신순
    // =====================================================

    @Transactional(readOnly = true)
    public List<Notification> getMyNotifications(
            String authorizationHeader
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        return notificationRepository
                .findByCustomerOrderByCreatedAtDesc(
                        customer
                );
    }


    // =====================================================
    // 읽지 않은 알림 개수 조회
    // =====================================================

    @Transactional(readOnly = true)
    public long getUnreadCount(
            String authorizationHeader
    ) {

        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        return notificationRepository
                .countByCustomerAndReadFalse(
                        customer
                );
    }


    // =====================================================
    // 특정 알림 읽음 처리
    // =====================================================

    @Transactional
    public void markAsRead(
            String authorizationHeader,
            Long notificationId
    ) {

        // 1. 현재 로그인 Customer
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 2. Notification 조회
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "알림을 찾을 수 없습니다."
                                )
                        );


        // 3. 본인 알림인지 확인
        if (
                !notification
                        .getCustomer()
                        .getId()
                        .equals(
                                customer.getId()
                        )
        ) {

            throw new RuntimeException(
                    "본인의 알림만 확인할 수 있습니다."
            );
        }


        // 4. 읽음 처리
        notification.markAsRead();
    }

    // =====================================================
// Support 채팅 종료 알림 생성
// =====================================================
    @Transactional
    public void createSupportClosedNotification(
            Customer customer,
            Long chatRoomId
    ) {

        Notification notification =
                new Notification(
                        customer,
                        NotificationType.SUPPORT_CLOSED,
                        "Support conversation closed",
                        "Your support conversation has been closed. You can still view the previous conversation, but you can no longer send messages.",
                        chatRoomId,
                        null
                );


        notificationRepository.save(
                notification
        );
    }


    // =====================================================
    // Support 채팅 자동 종료 알림 생성
    //
    // Customer -> Seller 승격 등에 사용
    // =====================================================
    @Transactional
    public void createSupportAutoClosedNotification(
            Customer customer,
            Long chatRoomId
    ) {

        Notification notification =
                new Notification(
                        customer,
                        NotificationType.SUPPORT_AUTO_CLOSED,
                        "Support conversation automatically closed",
                        "Your previous support conversation was automatically closed. If you need further assistance, please start a new support request.",
                        chatRoomId,
                        null
                );


        notificationRepository.save(
                notification
        );
    }
}