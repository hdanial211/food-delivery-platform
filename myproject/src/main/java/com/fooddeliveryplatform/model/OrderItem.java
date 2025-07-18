package com.fooddeliveryplatform.model;

import javax.json.Json;
import javax.json.JsonObject;

public class OrderItem {
    private Long orderItemId;
    private double pricePerUnit;
    private int quantity;
    private String specialInstructions;
    public Long getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    public Long getFoodItemId() {
        return foodItemId;
    }
    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    private Long foodItemId;
    private Long orderId;
    public OrderItem(Long orderItemId, double pricePerUnit, int quantity, String specialInstructions, Long foodItemId,
            Long orderId) {
        this.orderItemId = orderItemId;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.foodItemId = foodItemId;
        this.orderId = orderId;
    }

    public static OrderItem fromJson(JsonObject json) {
        Long orderItemId = json.getJsonNumber("orderItemId").longValue();
        double pricePerUnit = json.getJsonNumber("pricePerUnit").doubleValue();
        int quantity = json.getInt("quantity");
        String specialInstructions = json.getString("specialInstructions", null);
        Long foodItemId = json.getJsonNumber("foodItemId").longValue();
        Long orderId = json.getJsonNumber("orderId").longValue();

        return new OrderItem(orderItemId, pricePerUnit, quantity, specialInstructions, foodItemId, orderId);
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("orderItemId", orderItemId)
            .add("pricePerUnit", pricePerUnit)
            .add("quantity", quantity)
            .add("specialInstructions", specialInstructions != null ? specialInstructions : "")
            .add("foodItemId", foodItemId)
            .add("orderId", orderId)
            .build();
    }
}
