package com.example.admin.service;

import com.example.admin.dto.request.AdminLoginRequest;
import com.example.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // JwtTokenProvider를 자동으로 주입할 수 있게함
public class AdminAuthService {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final JwtTokenProvider jwtTokenProvider;


    public String authenticate(AdminLoginRequest request) { // void -> string 으로 로그인 검사만 하는게 아니라 JWT 문자열을 반환함

        if (!adminEmail.equals(request.getEmail()) // 이메일 or
                || !adminPassword.equals(request.getPassword())) { // pw가 다르면

            throw new IllegalArgumentException( // 로그인 불가능
                    "Invalid admin email or password"
            );
        }

        return jwtTokenProvider.createAdminToken(adminEmail);
    }


    public void validateAdminToken(String authorizationHeader) {

        // Authorization Header가 없거나
        // Bearer 형식이 아닌 경우
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            throw new RuntimeException("Admin token required");
        }

        // "Bearer " 부분 제거
        String token = authorizationHeader.substring(7);

        // JWT에서 role 꺼내기
        String role = jwtTokenProvider.getRoleFromToken(token);

        // ADMIN인지 확인
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Admin only");
        }
    }
}