package com.example.cart.service;

import com.example.cart.domain.CartItem;
import com.example.cart.repository.CartItemRepository;
import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.product.domain.Product;
import com.example.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.cart.dto.CartItemResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;


    @Transactional
    public void addToCart(
            Long customerId,
            Long productId
    ) {

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );


        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );


        cartItemRepository
                .findByCustomerIdAndProductId(
                        customerId,
                        productId
                )
                .ifPresentOrElse(

                        cartItem -> {

                            cartItem.setQuantity(
                                    cartItem.getQuantity() + 1
                            );

                        },

                        () -> {

                            CartItem cartItem =
                                    new CartItem();

                            cartItem.setCustomer(customer);

                            cartItem.setProduct(product);

                            cartItem.setQuantity(1);

                            cartItemRepository.save(
                                    cartItem
                            );
                        }
                );
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(
            Long customerId
    ) {

        return cartItemRepository
                .findByCustomerId(customerId)
                .stream()
                .map(cartItem -> {

                    Product product =
                            cartItem.getProduct();

                    return new CartItemResponse(
                            cartItem.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            cartItem.getQuantity()
                    );
                })
                .toList();
    }

    @Transactional
    public void updateQuantity(
            Long cartItemId,
            Integer quantity
    ) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );


        if (quantity < 1) {
            throw new RuntimeException(
                    "Quantity must be at least 1"
            );
        }


        cartItem.setQuantity(quantity);
    }

    @Transactional
    public void deleteCartItem(Long cartItemId) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        cartItemRepository.delete(cartItem);
    }

}