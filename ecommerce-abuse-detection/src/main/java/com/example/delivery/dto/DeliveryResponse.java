package com.example.delivery.dto;

import com.example.delivery.domain.Delivery;

import java.time.LocalDateTime;

public class DeliveryResponse {

    private Long deliveryId;
    private Long orderId;
    private String deliveryStatus;
    private String orderStatus;

    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;

    public DeliveryResponse(Delivery delivery) {

        this.deliveryId = delivery.getId();
        this.orderId = delivery.getOrder().getId();

        this.deliveryStatus =
                delivery.getStatus().name();

        this.orderStatus =
                delivery.getOrder().getStatus().name();

        this.assignedAt =
                delivery.getAssignedAt();

        this.pickedUpAt =
                delivery.getPickedUpAt();

        this.deliveredAt =
                delivery.getDeliveredAt();
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
}