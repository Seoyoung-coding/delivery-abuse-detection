package com.example.store.service;

import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.global.jwt.JwtTokenProvider;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;
import com.example.store.domain.Store;
import com.example.store.dto.request.StoreCreateRequest;
import com.example.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StoreService {

    // Customer를 찾기 위해 사용
    private final CustomerRepository customerRepository;

    // Customer와 연결된 Seller를 찾기 위해 사용
    private final SellerRepository sellerRepository;

    // Store 조회 / 저장을 위해 사용
    private final StoreRepository storeRepository;

    // JWT에서 현재 사용자의 email을 꺼내기 위해 사용
    private final JwtTokenProvider jwtTokenProvider;


    // =========================
    // 가게 등록
    // =========================
    public void createStore(
            String authorizationHeader,
            StoreCreateRequest request
    ) {

        // 1. Authorization Header가
        // "Bearer "로 시작하는지 확인
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {
            throw new RuntimeException("잘못된 토큰 형식입니다.");
        }


        // 2. 앞의 "Bearer "를 제거하고 실제 JWT만 추출
        String token =
                authorizationHeader.substring(7);


        // 3. JWT에서 현재 로그인한 사용자의 email 추출
        String email =
                jwtTokenProvider.getEmailFromToken(token);


        // 4. email을 이용해서 현재 로그인한 Customer 찾기
        Customer customer = customerRepository
                .findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "존재하지 않는 사용자입니다."
                        )
                );


        // 5. 현재 Customer와 연결된 Seller 찾기
        Seller seller = sellerRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new RuntimeException(
                                "판매자 등록이 필요합니다."
                        )
                );


        // 6. 이 Seller가 이미 Store를 가지고 있는지 확인
        if (storeRepository.existsBySeller(seller)) {
            throw new RuntimeException(
                    "이미 등록된 가게가 있습니다."
            );
        }


        // 7. Seller + 사용자가 입력한 가게 정보로 Store 생성
        Store store = new Store(
                seller,
                request.getName(),
                request.getDescription(),
                request.getAddress()
        );


        // 8. Store를 DB에 저장
        storeRepository.save(store);
    }
}