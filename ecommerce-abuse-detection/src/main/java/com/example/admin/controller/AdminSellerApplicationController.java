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
public class AdminSellerApplicationController {

    private final AdminSellerApplicationService adminSellerApplicationService;


    @GetMapping("/pending")
    public ResponseEntity<List<AdminSellerApplicationResponse>>
    getPendingApplications() {

        List<AdminSellerApplicationResponse> applications =
                adminSellerApplicationService.getPendingApplications();

        return ResponseEntity.ok(applications);
    }
}