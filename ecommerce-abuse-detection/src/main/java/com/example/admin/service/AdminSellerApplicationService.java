package com.example.admin.service;

import com.example.admin.dto.response.AdminSellerApplicationResponse;
import com.example.seller.domain.SellerApplication;
import com.example.seller.enums.SellerApplicationStatus;
import com.example.admin.repository.SellerApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSellerApplicationService {

    private final SellerApplicationRepository sellerApplicationRepository;

    public List<AdminSellerApplicationResponse> getPendingApplications() {
        List<SellerApplication> applications =
                sellerApplicationRepository.findByStatus(
                        SellerApplicationStatus.PENDING
                );

        return applications.stream() // List 안에 있는 객체를 하나씩 꺼내서 처리할 준비
                .map(AdminSellerApplicationResponse::new) // 각각의 데이터를 다른 형태로 바꿔라.
                .toList();
    }

    @Transactional
    public void approve(Long applicationId) {

        SellerApplication application =
                sellerApplicationRepository.findById(applicationId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Seller 신청을 찾을 수 없습니다."
                                )
                        );

        application.approve();
    }
}