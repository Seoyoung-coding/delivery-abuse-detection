package com.example.admin.config;

import com.example.admin.domain.Admin;
import com.example.admin.enums.AdminRole;
import com.example.admin.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AdminDataInitializer
        implements CommandLineRunner { //db에 어드민 계정을 넣기 위해서

    private final AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();


    // =========================
    // Seller 담당 Admin
    // =========================

    @Value("${admin.seller.email}")
    private String sellerAdminEmail;

    @Value("${admin.seller.password}")
    private String sellerAdminPassword;


    // =========================
    // Customer 담당 Admin
    // =========================

    @Value("${admin.customer.email}")
    private String customerAdminEmail;

    @Value("${admin.customer.password}")
    private String customerAdminPassword;


    @Override
    public void run(
            String... args
    ) {

        // =========================
        // Seller Admin 생성
        // =========================

        if (
                adminRepository
                        .findByEmail(
                                sellerAdminEmail
                        )
                        .isEmpty()
        ) {

            Admin sellerAdmin =
                    new Admin(
                            sellerAdminEmail,

                            passwordEncoder.encode(
                                    sellerAdminPassword
                            ),

                            AdminRole.SELLER_ADMIN
                    );

            adminRepository.save(
                    sellerAdmin
            );
        }


        // =========================
        // Customer Admin 생성
        // =========================

        if (
                adminRepository
                        .findByEmail(
                                customerAdminEmail
                        )
                        .isEmpty()
        ) {

            Admin customerAdmin =
                    new Admin(
                            customerAdminEmail,

                            passwordEncoder.encode(
                                    customerAdminPassword
                            ),

                            AdminRole.CUSTOMER_ADMIN
                    );

            adminRepository.save(
                    customerAdmin
            );
        }
    }
}