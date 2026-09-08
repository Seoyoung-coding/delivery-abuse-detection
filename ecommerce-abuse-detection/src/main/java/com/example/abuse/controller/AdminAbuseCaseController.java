package com.example.abuse.controller;

import com.example.abuse.dto.response.AbuseCaseResponse;
import com.example.abuse.service.AbuseCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/abuse-cases")
public class AdminAbuseCaseController {

    private final AbuseCaseService abuseCaseService;

    public AdminAbuseCaseController(
            AbuseCaseService abuseCaseService
    ) {
        this.abuseCaseService = abuseCaseService;
    }

    // Abuse Case 전체 조회
    @GetMapping
    public ResponseEntity<List<AbuseCaseResponse>> getAllCases() {

        return ResponseEntity.ok(
                abuseCaseService.getAllCases()
        );
    }


    // Abuse Case 상세 조회
    @GetMapping("/{caseId}")
    public ResponseEntity<AbuseCaseResponse> getCase(
            @PathVariable Long caseId
    ) {

        return ResponseEntity.ok(
                abuseCaseService.getCase(caseId)
        );
    }
}