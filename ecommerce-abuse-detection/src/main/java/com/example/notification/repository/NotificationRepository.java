package com.example.notification.repository;

import com.example.customer.domain.Customer;
import com.example.notification.domain.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository
        extends JpaRepository<Notification, Long> {


    // =====================================================
    // 특정 Customer의 알림 전체 조회
    // 최신 알림부터 보여줌
    // =====================================================

    List<Notification> findByCustomerOrderByCreatedAtDesc(
            Customer customer
    );


    // =====================================================
    // 특정 Customer의 읽지 않은 알림 개수
    // =====================================================

    long countByCustomerAndReadFalse(
            Customer customer
    );
}