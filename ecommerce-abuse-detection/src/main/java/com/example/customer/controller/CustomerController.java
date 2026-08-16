package com.example.customer.controller;

import com.example.customer.dto.request.LoginRequest;
import com.example.customer.dto.request.PasswordChangeRequest;
import com.example.customer.dto.request.SignupRequest;
import com.example.customer.dto.response.AuthResponse;
import com.example.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest request
    ) {
        customerService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        AuthResponse response = customerService.login(request);
        return ResponseEntity.ok(response);
    }

    // 1. 현재 로그인한 사용자 정보 요청
    @GetMapping("/me")
    public ResponseEntity<String> me(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        // 2. Authorization 헤더를 Service에 넘김
        String email = customerService.getMyEmail(authorizationHeader);

        // 3. 현재 로그인한 사용자의 이메일 반환
        return ResponseEntity.ok(email);
    }

    // 현재 로그인한 사용자의 비밀번호 변경
    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(

            // 1. JWT가 들어있는 Authorization Header 받기
            @RequestHeader("Authorization") String authorizationHeader,

            // 2. 사용자가 입력한 새 비밀번호 받기
            @RequestBody PasswordChangeRequest request
    ) {

        // 3. Service에 비밀번호 변경 요청
        customerService.changePassword(
                authorizationHeader,
                request
        );

        // 4. 변경 성공 응답
        return ResponseEntity.ok("비밀번호 변경 성공");
    }
}