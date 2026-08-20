package com.example.admin.controller;

import com.example.admin.dto.request.AdminLoginRequest;
import com.example.admin.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody AdminLoginRequest request
    ) {

        String token = adminAuthService.authenticate(request);

        return ResponseEntity.ok(token);
    }
}