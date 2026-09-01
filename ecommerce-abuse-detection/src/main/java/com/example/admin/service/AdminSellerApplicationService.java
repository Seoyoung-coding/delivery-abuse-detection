package com.example.admin.service;

import com.example.admin.dto.response.AdminSellerApplicationResponse;
import com.example.admin.repository.SellerApplicationRepository;

import com.example.seller.domain.Seller;
import com.example.seller.domain.SellerApplication;
import com.example.seller.enums.SellerApplicationStatus;
import com.example.seller.repository.SellerRepository;

import com.example.store.domain.Store;
import com.example.store.repository.StoreRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminSellerApplicationService {

    private final SellerApplicationRepository sellerApplicationRepository;

    private final SellerRepository sellerRepository;

    // Store 저장을 위해 추가
    private final StoreRepository storeRepository;


    // =====================================================
    // Admin : Pending Seller 신청 목록 조회
    // =====================================================
    public List<AdminSellerApplicationResponse> getPendingApplications() {

        List<SellerApplication> applications =
                sellerApplicationRepository.findByStatus(
                        SellerApplicationStatus.PENDING
                );


        return applications
                .stream()
                .map(AdminSellerApplicationResponse::new)
                .toList();
    }


    // =====================================================
    // Admin : Seller 신청 승인
    // =====================================================
    @Transactional
    public void approve(Long applicationId) {

        // 1. 신청 찾기
        SellerApplication application =
                sellerApplicationRepository
                        .findById(applicationId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Seller 신청을 찾을 수 없습니다."
                                )
                        );


        // 2. 이미 처리된 신청인지 확인
        if (
                application.getStatus()
                        != SellerApplicationStatus.PENDING
        ) {

            throw new RuntimeException(
                    "Already processed application"
            );
        }


        // 3. 신청 상태 APPROVED
        application.approve();


        // 4. Seller 생성
        Seller seller =
                new Seller(
                        application.getCustomer()
                );


        // 5. Seller DB 저장
        sellerRepository.save(
                seller
        );


        // 6. SellerApplication에서 입력했던 정보로 Store 생성
        Store store =
                new Store(
                        seller,
                        application.getStoreName(),
                        application.getDescription(),
                        application.getAddress()
                );


        // 7. Store DB 저장
        storeRepository.save(
                store
        );
    }


    // =====================================================
    // Admin : Seller 신청 거절
    // =====================================================
    @Transactional
    public void reject(Long applicationId) {

        // 1. 신청 찾기
        SellerApplication application =
                sellerApplicationRepository
                        .findById(applicationId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Seller application not found"
                                )
                        );


        // 2. 이미 처리된 신청인지 확인
        if (
                application.getStatus()
                        != SellerApplicationStatus.PENDING
        ) {

            throw new RuntimeException(
                    "Already processed application"
            );
        }


        // 3. 신청 거절
        application.reject();
    }
}