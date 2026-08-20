package com.example.global.jwt;

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
            Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));


    // =========================
    // 1. 일반 Customer 토큰 생성
    // =========================

    public String createToken(String email) {
        return createToken(email, "CUSTOMER");
    }


    // =========================
    // 2. Admin 토큰 생성
    // =========================

    public String createAdminToken(String email) {
        return createToken(email, "ADMIN");
    }



    private String createToken(
            String email,
            String role
    ) {

        Date now = new Date();

        Date expiration =
                new Date(now.getTime() + 1000 * 60 * 60);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }


    // =========================
    // JWT에서 email 꺼내기
    // =========================

    public String getEmailFromToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // =========================
    // JWT에서 role 꺼내기
    // =========================

    public String getRoleFromToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}