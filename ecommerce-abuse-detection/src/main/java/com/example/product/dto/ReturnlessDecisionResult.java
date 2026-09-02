package com.example.product.dto;

public record ReturnlessDecisionResult(

        double price,
        double productCost,
        double recoveryValue,
        double returnShippingCost,
        double handlingCost,
        double physicalReturnCost,
        String decision // 임시데이터들

) {
}
