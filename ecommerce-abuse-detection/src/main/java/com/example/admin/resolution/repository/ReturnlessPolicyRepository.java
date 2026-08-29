package com.example.admin.resolution.repository;

public class ReturnlessPolicyRepository {
}
// - returnless refund를 허용할 최대 가격 threshold 저장
// - policy version 저장
// - ACTIVE / CANDIDATE / INACTIVE 상태 저장
// - 정책 생성 시간 및 활성화 시간 저장
// - 기존 threshold를 덮어쓰지 않고 새로운 version으로 기록
// - 과거에 어떤 threshold가 사용되었는지 추적 가능하게 함