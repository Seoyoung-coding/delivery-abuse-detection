package com.example.admin.service;

import com.example.seller.domain.Seller;
import com.example.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSellerService {

    private final SellerRepository sellerRepository;


    // =====================================================
    // Admin : 승인된 Seller 전체 조회
    // =====================================================
    @Transactional(readOnly = true)
    public List<Seller> getAllSellers() {

        return sellerRepository.findAll();
    }
}