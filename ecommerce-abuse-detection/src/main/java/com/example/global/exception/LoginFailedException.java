package com.example.global.exception;

public class LoginFailedException extends RuntimeException {

    // 예외 메시지를 받을 생성자
    public LoginFailedException(String message) {

        // 부모 RuntimeException에게 메시지를 전달
        super(message);
    }
}