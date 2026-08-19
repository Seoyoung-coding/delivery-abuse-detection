package com.example.seller.domain;

import com.example.customer.domain.Customer;
import com.example.seller.enums.SellerApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SellerApplication {

    // 1. Seller 신청 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. 신청한 Customer
    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;

    // 3. 등록하려는 Store 이름
    @Column(nullable = false)
    private String storeName;

    // 4. Store 설명
    private String description;

    // 5. Store 주소
    private String address;


    // 6. 신청 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellerApplicationStatus status;

    // 7. Seller 신청 생성
    public SellerApplication(
            Customer customer,
            String storeName,
            String description,
            String address
    ) {

        this.customer = customer;
        this.storeName = storeName;
        this.description = description;
        this.address = address;

        // 처음 신청하면 무조건 심사 대기
        this.status = SellerApplicationStatus.PENDING;
    }

    // 8. 신청 승인
    public void approve() {

        if (this.status != SellerApplicationStatus.PENDING) {
            throw new IllegalStateException(
                    "이미 처리된 Seller 신청입니다."
            );
        }

        this.status = SellerApplicationStatus.APPROVED;
    }


    // 9. 신청 거절
    public void reject() {

        if (this.status != SellerApplicationStatus.PENDING) {
            throw new IllegalStateException(
                    "이미 처리된 Seller 신청입니다."
            );
        }

        this.status = SellerApplicationStatus.REJECTED;
    }
}