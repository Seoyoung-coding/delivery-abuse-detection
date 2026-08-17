package com.example.seller.service;

import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.global.jwt.JwtTokenProvider;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SellerService {


    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public void registerSeller(String authorizationHeader) {

        // 1. Authorization Header가
        // "Bearer "로 시작하는지 확인
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {
            throw new RuntimeException("잘못된 토큰 형식입니다.");
        }


        // 2. "Bearer " 7글자를 제거하고
        // 실제 JWT만 꺼냄
        String token =
                authorizationHeader.substring(7);


        // 3. JWT 안에서 현재 로그인한 사용자의
        // email을 꺼냄
        String email =
                jwtTokenProvider.getEmailFromToken(token);


        // 4. email을 이용해서
        // 현재 로그인한 Customer를 DB에서 찾음
        Customer customer = customerRepository
                .findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "존재하지 않는 사용자입니다."
                        )
                );


        // 5. 이 Customer가 이미 Seller인지 확인
        if (sellerRepository.existsByCustomer(customer)) {
            throw new RuntimeException(
                    "이미 판매자로 등록된 사용자입니다."
            );
        }


        // 6. 현재 Customer와 연결된 Seller 생성
        Seller seller =
                new Seller(customer);


        // 7. Seller를 DB에 저장
        sellerRepository.save(seller);
    }
}