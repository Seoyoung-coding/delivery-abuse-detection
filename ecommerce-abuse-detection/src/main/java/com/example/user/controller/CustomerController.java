package com.example.user.controller;

import com.example.user.dto.CustomerDto;
import com.example.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody CustomerDto.SignupRequest request
    ) {
        customerService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody CustomerDto.LoginRequest request
    ) {
        customerService.login(request);
        return ResponseEntity.ok("로그인 성공");
    }
}