package com.example.abuse.service;

import com.example.abuse.domain.AbuseCase;
import com.example.abuse.dto.response.CustomerAbuseResponse;
import com.example.abuse.repository.AbuseCaseRepository;
import com.example.customer.domain.Customer;
import com.example.refund.domain.Refund;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AbuseCaseService {

    private final AbuseCaseRepository abuseCaseRepository;

    public AbuseCaseService(
            AbuseCaseRepository abuseCaseRepository
    ) {
        this.abuseCaseRepository = abuseCaseRepository;
    }

    @Transactional
    public AbuseCase createCase(
            Customer customer,
            Refund refund,
            CustomerAbuseResponse abuse
    ) {

        AbuseCase abuseCase = new AbuseCase();

        abuseCase.setCustomer(customer);
        abuseCase.setRefund(refund);

        // 환불 요청 당시의 점수 저장
        abuseCase.setAbuseScore(
                abuse.getScore()
        );

        abuseCase.setRiskLevel(
                abuse.getRiskLevel()
        );

        return abuseCaseRepository.save(abuseCase);
    }
}