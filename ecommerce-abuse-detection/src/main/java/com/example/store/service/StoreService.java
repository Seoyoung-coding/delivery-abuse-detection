package com.example.store.service;

import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.CustomerService;
import com.example.global.jwt.JwtTokenProvider;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;
import com.example.store.domain.Store;
import com.example.store.dto.request.StoreCreateRequest;
import com.example.store.dto.response.StoreResponse;
import com.example.store.repository.StoreRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


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

    private final CustomerService customerService;


    // =========================
    // 가게 등록
    // =========================
    public void createStore(
            String authorizationHeader,
            StoreCreateRequest request
    ) {

        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {
            throw new RuntimeException("잘못된 토큰 형식입니다.");
        }
        String token =
                authorizationHeader.substring(7);
        String email =
                jwtTokenProvider.getEmailFromToken(token);

        Customer customer = customerRepository
                .findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "존재하지 않는 사용자입니다."
                        )
                );


        // 현재 Customer와 연결된 Seller 찾기
        Seller seller = sellerRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new RuntimeException(
                                "판매자 등록이 필요합니다."
                        )
                );


        // 이 Seller가 이미 Store를 가지고 있는지 확인
        if (storeRepository.existsBySeller(seller)) {
            throw new RuntimeException(
                    "이미 등록된 가게가 있습니다."
            );
        }


        // Seller + 사용자가 입력한 가게 정보로 Store 생성
        Store store = new Store(
                seller,
                request.getName(),
                request.getDescription(),
                request.getAddress()
        );


        // 8. Store를 DB에 저장
        storeRepository.save(store);
    }

    // =========================
    // 가게 대표 이미지 저장
    // =========================
    public String saveImage(
            String authorizationHeader,
            MultipartFile image
    ) {

        // 1. 현재 로그인한 Customer 찾기
        Customer customer =
                customerService.getCurrentCustomer(
                        authorizationHeader
                );


        // 2. 이 Customer가 Seller인지 확인
        Seller seller =
                sellerRepository
                        .findByCustomer(customer)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Seller만 가게 이미지를 변경할 수 있습니다."
                                )
                        );


        // 3. 이 Seller 본인이 소유한 Store 찾기
        Store store =
                storeRepository
                        .findBySeller(seller)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "등록된 가게가 없습니다."
                                )
                        );


        // 4. 이미지가 비어있는지 확인
        if (image == null || image.isEmpty()) {
            throw new RuntimeException(
                    "이미지를 선택해주세요."
            );
        }


        try {

            // 5. 원래 파일 이름
            String originalFilename =
                    image.getOriginalFilename();


            // 6. 중복되지 않는 파일 이름 생성
            String filename =
                    UUID.randomUUID()
                            + "_"
                            + originalFilename;


            // 7. 저장할 폴더
            Path uploadPath =
                    Paths.get(
                            "uploads",
                            "stores"
                    );


            // 8. 폴더가 없으면 생성
            Files.createDirectories(
                    uploadPath
            );


            // 9. 실제 저장 위치
            Path filePath =
                    uploadPath.resolve(
                            filename
                    );


            // 10. 이미지 파일 저장
            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            // 11. DB에 저장할 이미지 주소
            String imageUrl =
                    "/uploads/stores/"
                            + filename;


            // 12. 본인 Store의 imageUrl 변경
            store.updateImage(
                    imageUrl
            );


            // 13. 변경된 Store DB 저장
            storeRepository.save(
                    store
            );


            // 14. 이미지 주소 반환
            return imageUrl;


        } catch (IOException e) {

            throw new RuntimeException(
                    "이미지 저장 실패",
                    e
            );
        }
    }

    // =========================
    // 가게 상세 조회
    // =========================
    public StoreResponse getStore(Long storeId) {

        // 1. id로 Store 찾기
        Store store =
                storeRepository
                        .findById(storeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "존재하지 않는 가게입니다."
                                )
                        );


        // 2. Entity → Response DTO 변환
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getAddress(),
                store.getImageUrl()
        );
    }
}