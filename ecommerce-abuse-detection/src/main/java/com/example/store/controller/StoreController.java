package com.example.store.controller;

import com.example.store.dto.request.StoreCreateRequest;
import com.example.store.dto.response.StoreResponse;
import com.example.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    // 1. Store 관련 실제 로직을 처리하는 Service
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<String> createStore(

            // 2. 로그인한 사용자의 JWT 받기
            @RequestHeader("Authorization")
            String authorizationHeader,

            // 3. 사용자가 입력한 가게 정보 받기
            @RequestBody
            StoreCreateRequest request
    ) {

        // 4. JWT + 가게 정보를 Service로 전달
        storeService.createStore(
                authorizationHeader,
                request
        );


        // 5. 가게 등록 성공 응답
        return ResponseEntity.ok(
                "가게 등록 성공"
        );
    }

    @PatchMapping("/image")
    public ResponseEntity<String> uploadStoreImage(

            @RequestHeader("Authorization")
            String authorizationHeader,

            @RequestParam("image")
            MultipartFile image
    ) {

        String imageUrl =
                storeService.saveImage(
                        authorizationHeader,
                        image
                );

        return ResponseEntity.ok(imageUrl);
    }

    // =========================
    // 가게 상세 조회
    // =========================
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStore(
            @PathVariable Long storeId
    ) {

        StoreResponse store =
                storeService.getStore(storeId);

        return ResponseEntity.ok(store);
    }
}