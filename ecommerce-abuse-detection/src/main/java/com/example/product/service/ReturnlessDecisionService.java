package com.example.product.service;

import com.example.product.dto.ReturnlessDecisionResult;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ReturnlessDecisionService {

    private static final double COST_RATE = 0.5;

    public ReturnlessDecisionResult calculate(
            double price,
            double returnShippingCost,
            double handlingCost,
            double recoveryRate
    ) {

        // 판매가 × 원가율 = 상품 원가
        double productCost =
                price * COST_RATE;

        // 반품된 상품에서 다시 회수할 수 있는 가치
        double recoveryValue =
                productCost * recoveryRate;

        // 실제로 상품을 돌려받을 때 드는 순비용
        double physicalReturnCost =
                returnShippingCost
                        + handlingCost
                        - recoveryValue;

        String decision;

        if (physicalReturnCost > 0) {
            decision = "RETURNLESS";
        } else {
            decision = "RETURN_REQUIRED";
        }

        return new ReturnlessDecisionResult(
                price,
                productCost,
                recoveryValue,
                returnShippingCost,
                handlingCost,
                physicalReturnCost,
                decision
        );
    }

    // threshold 계산 매서드
    public double calculateThreshold(
            double returnShippingCost,
            double handlingCost,
            double recoveryRate
    ) {

        double totalReturnCost =
                returnShippingCost + handlingCost;

        return totalReturnCost
                / (COST_RATE * recoveryRate);
    }
}