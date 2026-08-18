package com.example.seller.service;

import com.example.customer.domain.Customer;
import com.example.seller.domain.SellerApplication;
import com.example.seller.enums.SellerApplicationStatus;
import com.example.seller.repository.SellerApplicationRepository;
import com.example.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerApplicationService {

    // =========================
    // 1. Repository
    // =========================

    private final SellerApplicationRepository sellerApplicationRepository;
    private final SellerRepository sellerRepository;


    // =========================
    // 2. Store 등록 신청
    // =========================

    public SellerApplication apply(
            Customer customer,
            String storeName,
            String description,
            String address
    ) {

        // -------------------------
        // 이미 승인된 Seller인지 확인
        // -------------------------

        if (sellerRepository.existsByCustomer(customer)) {

            throw new IllegalStateException(
                    "이미 Seller로 등록된 사용자입니다."
            );
        }


        // -------------------------
        // 이미 심사 중인 신청이 있는지 확인
        // -------------------------

        boolean alreadyPending =
                sellerApplicationRepository
                        .existsByCustomerAndStatus(
                                customer,
                                SellerApplicationStatus.PENDING
                        );


        if (alreadyPending) {

            throw new IllegalStateException(
                    "이미 심사 중인 Seller 신청이 있습니다."
            );
        }


        // -------------------------
        // 새로운 Seller 신청 생성
        // -------------------------

        SellerApplication application =
                new SellerApplication(
                        customer,
                        storeName,
                        description,
                        address
                );


        // SellerApplication 생성자에서
        // status는 자동으로 PENDING이 됨


        // -------------------------
        // DB 저장
        // -------------------------

        return sellerApplicationRepository.save(
                application
        );
    }


    // =========================
    // 3. 현재 Customer의
    // Seller 신청 상태 조회
    // =========================

    public String getApplicationStatus(
            Customer customer
    ) {

        // -------------------------
        // 이미 Seller라면
        // 승인 완료 상태
        // -------------------------

        if (sellerRepository.existsByCustomer(customer)) {

            return "APPROVED";
        }


        // -------------------------
        // 가장 최근 신청 조회
        // -------------------------

        return sellerApplicationRepository
                .findTopByCustomerOrderByIdDesc(
                        customer
                )

                .map(
                        application ->
                                application
                                        .getStatus()
                                        .name()
                )

                // 신청 기록 자체가 없으면
                // NONE 반환
                .orElse("NONE");
    }
}