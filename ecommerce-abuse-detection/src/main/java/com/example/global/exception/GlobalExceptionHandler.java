package com.example.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
// Service에서 발생한 Java Exception을 실제 HTTP 응답으로 번역해주는 역할이 Handler
// 프론트엔드/Postman한테 어떻게 응답할지 결정한다
public class GlobalExceptionHandler {


    // 1. 로그인 실패
    // 이메일 또는 비밀번호가 틀린 경우
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleLoginFailed(
            LoginFailedException e
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }


    // 2. 이메일 중복
    // 이미 가입된 이메일로 다시 회원가입한 경우
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(
            DuplicateEmailException e
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }


    // 3. JWT / Authorization 문제
    // Bearer 형식이 잘못됐거나 토큰이 만료/위조된 경우
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidToken(
            InvalidTokenException e
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }


    // 4. Customer를 찾을 수 없는 경우
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(
            CustomerNotFoundException e
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }


    // 5. 잘못된 비밀번호 입력
    // 예: 새 비밀번호가 null 또는 빈 문자열
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPassword(
            InvalidPasswordException e
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}