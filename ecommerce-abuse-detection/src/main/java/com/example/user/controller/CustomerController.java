package com.example.user.controller;

import com.example.user.dto.CustomerDto;
import com.example.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}