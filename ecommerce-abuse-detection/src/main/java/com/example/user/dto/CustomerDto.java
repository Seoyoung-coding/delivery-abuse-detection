package com.example.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CustomerDto { // 프론트엔드와 이메일, 비밀번호를 주고받을 데이터 상자
    @Getter
    @NoArgsConstructor
    public static class SignupRequest {
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
    private String email;
    private String password;
    }

    @Getter
    public static class AuthResponse {
        private String message;
        public AuthResponse(String message) {
            this.message = message;
        }
    }
}

