package com.example.admin.controller;

import com.example.admin.dto.response.AdminSellerApplicationResponse;
import com.example.admin.service.AdminSellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/seller-applications")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = {
                "Authorization",
                "Content-Type"
        }
)
public class AdminSellerApplicationController {

    private final AdminSellerApplicationService adminSellerApplicationService;


    // =========================
    // 1. 대기 중 Seller 신청 조회
    // =========================

    @GetMapping("/pending")
    public ResponseEntity<List<AdminSellerApplicationResponse>>
    getPendingApplications() {

        List<AdminSellerApplicationResponse> applications =
                adminSellerApplicationService.getPendingApplications();

        return ResponseEntity.ok(applications);
    }


    // =========================
    // 2. Seller 신청 승인
    // =========================

    @PatchMapping("/{applicationId}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long applicationId
    ) {

        adminSellerApplicationService.approve(applicationId);

        return ResponseEntity.ok().build();
    }

    // =========================
    // 3. Seller 신청 거절
    // =========================

    @PatchMapping("/{applicationId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long applicationId
    ) {

        adminSellerApplicationService.reject(applicationId);

        return ResponseEntity.ok().build();
    }
}