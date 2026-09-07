package com.example.order.dto.request;

public class CreateOrderRequest {

    // 주문할 상품 목록
    private List<OrderItemRequest> items;

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}