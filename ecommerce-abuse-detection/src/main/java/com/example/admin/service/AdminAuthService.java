package com.example.admin.service;

import com.example.admin.domain.Admin;
import com.example.admin.dto.request.AdminLoginRequest;
import com.example.admin.repository.AdminRepository;
import com.example.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();


    // =====================================================
    // Admin 로그인
    //
    // DB에서 email로 Admin을 찾고
    // 비밀번호 확인 후
    // 해당 Admin의 role이 들어간 JWT 발급
    // =====================================================
    public String authenticate(
            AdminLoginRequest request
    ) {

        // 1. 이메일로 Admin 찾기
        Admin admin =
                adminRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Invalid admin email or password"
                                        )
                        );


        // 2. 비밀번호 확인
        if (
                !passwordEncoder.matches(
                        request.getPassword(),
                        admin.getPassword()
                )
        ) {

            throw new IllegalArgumentException(
                    "Invalid admin email or password"
            );
        }


        // 3. Admin 역할에 맞는 JWT 발급
        //
        // SELLER_ADMIN
        // 또는
        // CUSTOMER_ADMIN
        return jwtTokenProvider
                .createAdminToken(
                        admin.getEmail(),
                        admin.getRole()
                );
    }


    // =====================================================
    // 공통 Admin JWT 검증
    //
    // SELLER_ADMIN / CUSTOMER_ADMIN
    // 둘 중 하나면 Admin으로 인정
    // =====================================================
    public void validateAdminToken(
            String authorizationHeader
    ) {

        // 1. Authorization Header 확인
        if (
                authorizationHeader == null
                        ||
                        !authorizationHeader.startsWith(
                                "Bearer "
                        )
        ) {

            throw new RuntimeException(
                    "Admin token required"
            );
        }


        // 2. Bearer 제거
        String token =
                authorizationHeader.substring(7);


        // 3. JWT에서 role 가져오기
        String role =
                jwtTokenProvider
                        .getRoleFromToken(
                                token
                        );


        // 4. Admin 역할인지 확인
        boolean isAdmin =
                "SELLER_ADMIN".equals(role)
                        ||
                        "CUSTOMER_ADMIN".equals(role);


        if (!isAdmin) {

            throw new RuntimeException(
                    "Admin only"
            );
        }
    }
    // =====================================================
    // 현재 로그인한 Admin 조회
    // =====================================================
    public Admin getCurrentAdmin(
            String authorizationHeader
    ) {

        // 1. Bearer Token 확인
        if (
                authorizationHeader == null
                        ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {

            throw new RuntimeException(
                    "Admin token required"
            );
        }


        // 2. JWT 추출
        String token =
                authorizationHeader.substring(7);


        // 3. JWT에서 Admin email 추출
        String email =
                jwtTokenProvider
                        .getEmailFromToken(token);


        // 4. DB에서 실제 Admin 조회
        Admin admin =
                adminRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Admin account not found"
                                )
                        );


        // 5. JWT의 role과 DB role이 같은지 확인
        String tokenRole =
                jwtTokenProvider
                        .getRoleFromToken(token);


        if (
                !admin.getRole()
                        .name()
                        .equals(tokenRole)
        ) {

            throw new RuntimeException(
                    "Invalid admin role"
            );
        }


        return admin;
    }

}