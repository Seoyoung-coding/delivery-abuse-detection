package com.example.abuse.enums;

public enum AbuseCaseStatus {

    // 아직 Admin이 확인하지 않음
    OPEN,

    // Admin이 검토 중
    UNDER_REVIEW,

    // 정상 고객으로 판단
    CLEARED,

    // 어뷰징으로 판단
    CONFIRMED
}