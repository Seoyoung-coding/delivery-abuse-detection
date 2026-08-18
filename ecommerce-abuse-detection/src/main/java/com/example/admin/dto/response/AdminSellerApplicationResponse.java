package com.example.admin.dto.response;

import com.example.seller.domain.SellerApplication;
import com.example.seller.enums.SellerApplicationStatus;
import lombok.Getter;

@Getter
public class AdminSellerApplicationResponse {

    private final Long id;
    private final String storeName;
    private final String description;
    private final String address;
    private final String customerEmail;
    private final SellerApplicationStatus status;


    public AdminSellerApplicationResponse(
            SellerApplication application
    ) {
        this.id = application.getId();
        this.storeName = application.getStoreName();
        this.description = application.getDescription();
        this.address = application.getAddress();
        this.customerEmail =
                application.getCustomer().getEmail();
        this.status = application.getStatus();
    }
}