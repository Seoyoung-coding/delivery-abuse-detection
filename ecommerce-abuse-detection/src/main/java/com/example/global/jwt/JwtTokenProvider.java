package com.example.global.jwt;

import com.example.admin.enums.AdminRole;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String secretKey =
            "yamiyumi-secret-key-must-be-at-least-32-bytes-long";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    secretKey.getBytes(
                            StandardCharsets.UTF_8
                    )
            );


    // =========================
    // 1. 일반 Customer 토큰 생성
    // =========================

    public String createToken(
            String email
    ) {

        return createToken(
                email,
                "CUSTOMER"
        );
    }


    // =========================
    // 2. 기존 Admin 토큰 생성
    // =========================
    //
    // 기존 AdminAuthService가 아직
    // createAdminToken(email)을 사용하고 있으므로
    // 당장은 유지
    // =========================

    public String createAdminToken(
            String email
    ) {

        return createToken(
                email,
                "ADMIN"
        );
    }


    // =========================
    // 3. 역할별 Admin 토큰 생성
    // =========================

    public String createAdminToken(
            String email,
            AdminRole role
    ) {

        return createToken(
                email,
                role.name()
        );
    }


    // =========================
    // 4. 실제 JWT 생성 공통 메서드
    // =========================

    private String createToken(
            String email,
            String role
    ) {

        Date now =
                new Date();

        Date expiration =
                new Date(
                        now.getTime()
                                + 1000 * 60 * 60
                );


        return Jwts.builder()

                // 로그인 사용자 email
                .subject(email)

                // 권한
                .claim(
                        "role",
                        role
                )

                // 토큰 발급 시간
                .issuedAt(now)

                // 만료 시간
                .expiration(expiration)

                // 서명
                .signWith(key)

                // JWT 문자열 생성
                .compact();
    }


    // =========================
    // 5. JWT에서 email 꺼내기
    // =========================

    public String getEmailFromToken(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // =========================
    // 6. JWT에서 role 꺼내기
    // =========================

    public String getRoleFromToken(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(
                        "role",
                        String.class
                );
    }
}