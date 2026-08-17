package com.example.product.repository;

import com.example.product.domain.Product;
import com.example.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository
        extends JpaRepository<Product, Long> {


    // 1. 특정 Store에 등록된 모든 상품 조회
    //
    // 예:
    // YAMI Burger Store
    // → Cheese Burger
    // → Fries
    // → Coke
    //
    // 를 전부 List<Product> 형태로 가져옴
    List<Product> findAllByStore(Store store);
}