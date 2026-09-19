package com.example.cart.controller;

import com.example.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cart.dto.CartItemResponse;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam Long customerId,
            @RequestParam Long productId
    ) {

        cartService.addToCart(
                customerId,
                productId
        );

        return ResponseEntity.ok(
                "Added to cart"
        );
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(
            @RequestParam Long customerId
    ) {

        List<CartItemResponse> cart =
                cartService.getCart(customerId);

        return ResponseEntity.ok(cart);
    }
}