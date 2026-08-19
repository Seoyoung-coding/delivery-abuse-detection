package com.example.admin.service;

import com.example.admin.dto.request.AdminLoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;


    public void authenticate(AdminLoginRequest request) {

        if (!adminEmail.equals(request.getEmail()) // 이메일이 다르거나
                || !adminPassword.equals(request.getPassword())) { //pw가 다르면

            throw new IllegalArgumentException( // 로그인 실패
                    "Invalid admin email or password"
            );
        }
    }
}