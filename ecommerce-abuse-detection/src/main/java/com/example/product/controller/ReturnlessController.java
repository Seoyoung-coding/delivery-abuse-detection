package com.example.product.controller;

import com.example.product.dto.ReturnlessDecisionResult;
import com.example.product.service.ReturnlessDecisionService;
import org.springframework.web.bind.annotation.*;

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
    public ReturnlessDecisionResult getDecision(
            @RequestParam double price,
            @RequestParam double returnShippingCost,
            @RequestParam double handlingCost,
            @RequestParam double recoveryRate
    ) {
        return returnlessDecisionService.calculate(
                price,
                returnShippingCost,
                handlingCost,
                recoveryRate
        );
    }

    @GetMapping("/threshold")
    public double getThreshold(
            @RequestParam double returnShippingCost,
            @RequestParam double handlingCost,
            @RequestParam double recoveryRate
    ) {
        return returnlessDecisionService.calculateThreshold(
                returnShippingCost,
                handlingCost,
                recoveryRate
        );
    }
}