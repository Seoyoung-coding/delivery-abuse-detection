package com.example.global.exception;


// 잘못된 비밀번호 입력 예외
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}