package com.example.seller.controller;

import com.example.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerSeller(

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        sellerService.registerSeller(
                authorizationHeader
        );


        // 4. Seller 등록이 정상적으로 끝나면 성공 응답
        return ResponseEntity.ok(
                "판매자 등록 성공"
        );
    }
}