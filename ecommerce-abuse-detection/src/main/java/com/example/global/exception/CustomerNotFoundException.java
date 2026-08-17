package com.example.global.exception;


// Customer를 찾지 못한 경우
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}