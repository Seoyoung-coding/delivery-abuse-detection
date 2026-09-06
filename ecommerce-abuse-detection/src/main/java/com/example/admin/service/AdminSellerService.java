package com.example.admin.service;

import com.example.product.repository.ProductRepository;
import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;
import com.example.store.domain.Store;
import com.example.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminSellerService {

    private final SellerRepository sellerRepository;

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    // =====================================================
    // Admin : 승인된 Seller 전체 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<Seller> getAllSellers() {

        return sellerRepository.findAll();
    }


    // =====================================================
    // Admin : Seller가 소유한 Store 조회
    // =====================================================
    @Transactional(readOnly = true)
    public Store getStoreBySeller(
            Seller seller
    ) {

        return storeRepository
                .findBySeller(seller)
                .orElse(null);
    }

    // =====================================================
    // Admin : 특정 Store의 등록 상품 개수
    // =====================================================
    @Transactional(readOnly = true)
    public long getProductCount(
            Store store
    ) {

        if (store == null) {
            return 0;
        }

        return productRepository
                .countByStore(store);
    }
}