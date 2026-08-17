package com.example.global.exception;


// 이미 사용 중인 이메일 예외
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}