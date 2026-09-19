package com.example.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateOrderRequest {
        private List<OrderItemRequest> items;
        public List<OrderItemRequest> getItems() {
            return items;
        }
    }
