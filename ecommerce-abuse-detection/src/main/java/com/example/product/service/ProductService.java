package com.example.product.service;

import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.global.jwt.JwtTokenProvider;
import com.example.product.domain.Product;
import com.example.product.dto.request.ProductCreateRequest;
import com.example.product.repository.ProductRepository;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;
import com.example.store.domain.Store;
import com.example.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    // Customer 조회
    private final CustomerRepository customerRepository;

    // Seller 조회
    private final SellerRepository sellerRepository;

    // Store 조회
    private final StoreRepository storeRepository;

    // Product 저장
    private final ProductRepository productRepository;

    // JWT에서 email 추출
    private final JwtTokenProvider jwtTokenProvider;


    // =========================
    // 상품 등록
    // =========================
    public void createProduct(
            String authorizationHeader,
            ProductCreateRequest request
    ) {

        // 1. Authorization Header가 Bearer 형식인지 확인
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {
            throw new RuntimeException(
                    "잘못된 토큰 형식입니다."
            );
        }


        // 2. "Bearer " 7글자를 제거하고 실제 JWT만 꺼냄
        String token =
                authorizationHeader.substring(7);


        // 3. JWT에서 현재 로그인한 사용자의 email 추출
        String email =
                jwtTokenProvider.getEmailFromToken(token);


        // 4. email로 현재 로그인한 Customer 찾기
        Customer customer = customerRepository
                .findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "존재하지 않는 사용자입니다."
                        )
                );


        // 5. 이 Customer가 Seller인지 확인하고 Seller 가져오기
        Seller seller = sellerRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new RuntimeException(
                                "판매자만 상품을 등록할 수 있습니다."
                        )
                );


        // 6. 이 Seller가 소유한 Store 찾기
        Store store = storeRepository
                .findBySeller(seller)
                .orElseThrow(() ->
                        new RuntimeException(
                                "등록된 가게가 없습니다."
                        )
                );


        // 7. Store + Seller가 입력한 상품 정보로 Product 생성
        Product product = new Product(
                store,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl()
        );


        // 8. Product를 DB에 저장
        productRepository.save(product);
    }
}