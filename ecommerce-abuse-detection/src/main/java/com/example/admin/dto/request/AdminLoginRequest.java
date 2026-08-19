package com.example.admin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequest { // json 요청을 받는 역할

    private String email;
    private String password;
}