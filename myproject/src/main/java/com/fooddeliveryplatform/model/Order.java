package com.fooddeliveryplatform.model;

import java.sql.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Order {
    private Long orderId;
    private Date createdAt;
    private String deliveryAddress;
    private OrderStatus status;
    private double totalAmount;
    private Long customerId;
    private Long restaurantId;
    public Order(Long orderId, Date createdAt, String deliveryAddress, OrderStatus status, double totalAmount,
             Long customerId, Long restaurantId) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
    }
    public Order(Date createdAt, String deliveryAddress, OrderStatus status, double totalAmount, Long customerId,
            Long restaurantId) {
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public Long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
            .add("orderId", orderId)
            .add("createdAt", createdAt.toString())
            .add("deliveryAddress", deliveryAddress)
            .add("status", status.toString())
            .add("totalAmount", totalAmount)
            .add("customerId", customerId)
            .add("restaurantId", restaurantId);
        
        return builder.build();
    }

    public static Order fromJson(JsonObject json) {
        Date createdAt = Date.valueOf(json.getString("createdAt"));
        String deliveryAddress = json.getString("deliveryAddress");
        OrderStatus status = OrderStatus.valueOf(json.getString("status"));
        double totalAmount = json.getJsonNumber("totalAmount").doubleValue();
        Long customerId = json.getJsonNumber("customerId").longValue();
        Long restaurantId = json.getJsonNumber("restaurantId").longValue();
        

        
        return new Order(createdAt, deliveryAddress, status, totalAmount, customerId, restaurantId);
    }
}
