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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // =========================
    // 상품 등록
    // =========================
    public void createProduct(
            String authorizationHeader,
            ProductCreateRequest request
    ) {

        // 1. Bearer 형식 확인
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {
            throw new RuntimeException(
                    "잘못된 토큰 형식입니다."
            );
        }


        // 2. Bearer 제거하고 JWT 추출
        String token =
                authorizationHeader.substring(7);


        // 3. JWT에서 email 추출
        String email =
                jwtTokenProvider.getEmailFromToken(token);


        // 4. 현재 로그인한 Customer 찾기
        Customer customer = customerRepository
                .findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "존재하지 않는 사용자입니다."
                        )
                );


        // 5. Seller인지 확인
        Seller seller = sellerRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new RuntimeException(
                                "판매자만 상품을 등록할 수 있습니다."
                        )
                );


        // 6. Seller의 Store 찾기
        Store store = storeRepository
                .findBySeller(seller)
                .orElseThrow(() ->
                        new RuntimeException(
                                "등록된 가게가 없습니다."
                        )
                );


        // 7. Seller가 올린 사진 저장
        String imageUrl =
                saveImage(request.getImage());


        // 8. Product 생성
        Product product = new Product(
                store,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                imageUrl
        );


        // 9. Product DB 저장
        productRepository.save(product);
    }


    // =========================
    // 상품 이미지 저장
    // =========================
    private String saveImage(MultipartFile image) {

        // 1. 사진이 없는 경우
        if (image == null || image.isEmpty()) {
            return null;
        }


        try {

            // 2. 사진을 저장할 폴더
            Path uploadDirectory =
                    Paths.get("uploads/products");


            // 3. 폴더가 없으면 자동 생성
            Files.createDirectories(uploadDirectory);


            // 4. 파일 이름이 겹치지 않도록 UUID 생성
            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + image.getOriginalFilename();


            // 5. 최종 저장 위치
            Path filePath =
                    uploadDirectory.resolve(fileName);


            // 6. 실제 파일 저장
            image.transferTo(filePath);


            // 7. DB에 저장할 이미지 URL 반환
            return "/uploads/products/" + fileName;


        } catch (IOException e) {

            throw new RuntimeException(
                    "이미지 저장에 실패했습니다."
            );
        }
    }
}