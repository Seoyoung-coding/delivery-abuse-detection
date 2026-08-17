package com.example.store.repository;

import com.example.seller.domain.Seller;
import com.example.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StoreRepository
        extends JpaRepository<Store, Long> {


    // 1. 이 Seller가 이미 가게를 등록했는지 확인
    boolean existsBySeller(Seller seller);


    // 2. Seller가 소유한 Store 찾기
    Optional<Store> findBySeller(Seller seller);
}