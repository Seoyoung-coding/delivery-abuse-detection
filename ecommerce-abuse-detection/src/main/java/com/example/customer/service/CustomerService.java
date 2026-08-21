package com.example.customer.service;

import com.example.customer.domain.Customer;
import com.example.customer.dto.request.LoginRequest;
import com.example.customer.dto.request.PasswordChangeRequest;
import com.example.customer.dto.request.SignupRequest;
import com.example.customer.dto.response.AuthResponse;
import com.example.customer.repository.CustomerRepository;

import com.example.global.exception.CustomerNotFoundException;
import com.example.global.exception.DuplicateEmailException;
import com.example.global.exception.InvalidPasswordException;
import com.example.global.exception.InvalidTokenException;
import com.example.global.exception.LoginFailedException;

import com.example.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    // =========================
    // 회원가입
    // =========================
    public void signup(SignupRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();


        // 이메일 중복 예외
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(
                    "이미 사용 중인 이메일입니다."
            );
        }


        String encodedPassword =
                passwordEncoder.encode(password);


        Customer customer = new Customer(
                email,
                encodedPassword
        );


        customerRepository.save(customer);
    }


    // =========================
    // 로그인
    // =========================
    public AuthResponse login(LoginRequest request) {

        // 1. 탈퇴하지 않은 회원 중 이메일 검색
        Customer customer = customerRepository
                .findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() ->
                        new LoginFailedException(
                                "이메일 또는 비밀번호가 올바르지 않습니다."
                        )
                );


        // 2. 비밀번호 비교
        boolean passwordMatch =
                passwordEncoder.matches(
                        request.getPassword(),
                        customer.getPassword()
                );


        // 3. 비밀번호가 틀리면 로그인 실패
        if (!passwordMatch) {

            throw new LoginFailedException(
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }


        // 4. 로그인 성공 → JWT 생성
        String token =
                jwtTokenProvider.createToken(
                        customer.getEmail()
                );


        // 5. 응답 반환
        return new AuthResponse(
                "Login Success",
                token
        );
    }


    // =========================
    // 현재 로그인한 사용자 이메일 조회
    // =========================
    public String getMyEmail(
            String authorizationHeader
    ) {

        Customer customer =
                getCurrentCustomer(authorizationHeader);

        return customer.getEmail();
    }


    public void changePassword(
            String authorizationHeader,
            PasswordChangeRequest request
    ) {

        // =========================
        // 1. 현재 로그인 Customer 확인
        // =========================

        Customer customer =
                getCurrentCustomer(authorizationHeader);


        // =========================
        // 2. 현재 비밀번호 입력 확인
        // =========================

        if (
                request.getCurrentPassword() == null ||
                        request.getCurrentPassword().isBlank()
        ) {

            throw new InvalidPasswordException(
                    "현재 비밀번호를 입력해주세요."
            );
        }


        // =========================
        // 3. 현재 비밀번호가 실제 비밀번호와 같은지 확인
        // =========================

        if (
                !passwordEncoder.matches(
                        request.getCurrentPassword(),
                        customer.getPassword()
                )
        ) {

            throw new InvalidPasswordException(
                    "현재 비밀번호가 올바르지 않습니다."
            );
        }


        // =========================
        // 4. 새 비밀번호 입력 확인
        // =========================

        if (
                request.getNewPassword() == null ||
                        request.getNewPassword().isBlank()
        ) {

            throw new InvalidPasswordException(
                    "새 비밀번호를 입력해주세요."
            );
        }


        // =========================
        // 5. 새 비밀번호 암호화
        // =========================

        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword()
                );


        // =========================
        // 6. 비밀번호 변경
        // =========================

        customer.changePassword(
                encodedPassword
        );


        // =========================
        // 7. DB 저장
        // =========================

        customerRepository.save(customer);
    }


    // =========================
    // 회원 탈퇴
    // =========================
    public void deleteMyAccount(
            String authorizationHeader
    ) {

        Customer customer =
                getCurrentCustomer(authorizationHeader);


        customer.softDelete();


        customerRepository.save(customer);
    }


    // =========================
    // 공통 로직
    // 현재 로그인한 Customer 찾기
    // =========================
    public Customer getCurrentCustomer(
            String authorizationHeader
    ) {

        // 1. Authorization Header 형식 문제
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {

            throw new InvalidTokenException(
                    "잘못된 토큰 형식입니다."
            );
        }


        // 2. Bearer 제거
        String token =
                authorizationHeader.substring(7);


        // 3. JWT에서 email 추출
        String email;

        try {

            email =
                    jwtTokenProvider.getEmailFromToken(token);

        } catch (Exception e) {

            // 만료 / 위조 / 잘못된 JWT
            throw new InvalidTokenException(
                    "유효하지 않거나 만료된 토큰입니다."
            );
        }


        // 4. Customer가 존재하지 않는 경우
        return customerRepository
                .findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "존재하지 않는 사용자입니다."
                        )
                );
    }
}