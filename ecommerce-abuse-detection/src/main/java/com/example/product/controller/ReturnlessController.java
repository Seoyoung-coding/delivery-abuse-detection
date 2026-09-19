package com.example.product.controller;

import com.example.refund.enums.RefundResolution;
import com.example.refund.service.ReturnlessDecisionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/returnless")
public class ReturnlessController {

    private final ReturnlessDecisionService returnlessDecisionService;

    public ReturnlessController(
            ReturnlessDecisionService returnlessDecisionService
    ) {
        this.returnlessDecisionService = returnlessDecisionService;
    }

    @GetMapping("/decision")
    public RefundResolution getDecision(
            @RequestParam BigDecimal orderAmount,
            @RequestParam double abuseScore
    ) {
        return returnlessDecisionService.decide(
                orderAmount,
                abuseScore
        );
    }
}