package com.example.global.exception;


// JWT / Authorization 관련 예외
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}