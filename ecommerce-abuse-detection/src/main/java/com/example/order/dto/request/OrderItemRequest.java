package com.example.order.dto.request;

public class OrderItemRequest {

    // 구매할 상품 ID
    private Long productId;

    // 구매 수량
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}