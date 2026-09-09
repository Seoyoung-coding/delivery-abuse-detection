package com.example.refund.service;

import com.example.refund.enums.RefundResolution;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReturnlessDecisionService {

    // 임시 반품 배송비
    private static final BigDecimal RETURN_SHIPPING_COST =
            BigDecimal.valueOf(8.00);

    // 검수, 재포장 등 처리 비용
    private static final BigDecimal PROCESSING_COST =
            BigDecimal.valueOf(5.00);

    // 반품 상품을 다시 판매했을 때 회수 가능한 비율
    // 예: $100 상품 → 약 $70 가치 회수
    private static final double RECOVERY_RATE = 0.70;

    // HIGH 이상 고객은 returnless를 허용하지 않음
    private static final double HIGH_RISK_THRESHOLD = 60.0;


    public RefundResolution decide(
            BigDecimal orderAmount,
            double abuseScore
    ) {

        /*
         * 1. Abuse 위험이 높은 고객
         *
         * 상품도 가지고 돈도 환불받는 returnless를
         * 반복적으로 악용할 가능성이 있으므로
         * 강제로 실제 반품 요구
         */
        if (abuseScore >= HIGH_RISK_THRESHOLD) {
            return RefundResolution.RETURN_REQUIRED;
        }


        /*
         * 2. 반품 처리에 들어가는 총 비용
         */
        BigDecimal returnCost =
                RETURN_SHIPPING_COST
                        .add(PROCESSING_COST);


        /*
         * 3. 실제 상품을 돌려받았을 때
         * 다시 판매해서 회수할 수 있는 예상 가치
         */
        BigDecimal recoveryValue =
                orderAmount.multiply(
                        BigDecimal.valueOf(RECOVERY_RATE)
                );


        /*
         * 4. 회수 가치보다 반품 비용이 더 크면
         * 굳이 상품을 돌려받는 것이 손해
         */
        if (recoveryValue.compareTo(returnCost) <= 0) {

            return RefundResolution.RETURNLESS;
        }


        /*
         * 상품을 회수하는 것이 경제적으로 더 이득
         */
        return RefundResolution.RETURN_REQUIRED;
    }
}