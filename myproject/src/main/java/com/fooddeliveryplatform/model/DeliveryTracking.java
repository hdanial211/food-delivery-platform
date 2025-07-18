package com.fooddeliveryplatform.model;

import javax.json.Json;
import javax.json.JsonObject;

public class DeliveryTracking {
    private Long trackingId;
    private Long orderId;
    private String status;
    private String estimatedDeliveryTime;
    private String currentLocation;
    private Long order_id;
    private Long delivery_partner_id;

    public DeliveryTracking() {}

    public DeliveryTracking(Long trackingId, Long orderId, String status, String estimatedDeliveryTime, 
                            String currentLocation, Long order_id, Long delivery_partner_id) {
        this.trackingId = trackingId;
        this.orderId = orderId;
        this.status = status;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.currentLocation = currentLocation;
        this.order_id = order_id;
        this.delivery_partner_id = delivery_partner_id;
    }


    public Long getTrackingId() {
        return trackingId;
    }
    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }
    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
    public String getCurrentLocation() {
        return currentLocation;
    }
    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
    public Long getOrder_id() {
        return order_id;
    }
    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }
    public Long getDelivery_partner_id() {
        return delivery_partner_id;
    }
    public void setDelivery_partner_id(Long delivery_partner_id) {
        this.delivery_partner_id = delivery_partner_id;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("trackingId", trackingId)
            .add("orderId", orderId)
            .add("status", status)
            .add("estimatedDeliveryTime", estimatedDeliveryTime)
            .add("currentLocation", currentLocation)
            .add("order_id", order_id)
            .add("delivery_partner_id", delivery_partner_id)
            .build();
    }

    public static DeliveryTracking fromJson(JsonObject json) {
        DeliveryTracking tracking = new DeliveryTracking();
        tracking.setTrackingId(json.getJsonNumber("trackingId").longValue());
        tracking.setOrderId(json.getJsonNumber("orderId").longValue());
        tracking.setStatus(json.getString("status"));
        tracking.setEstimatedDeliveryTime(json.getString("estimatedDeliveryTime"));
        tracking.setCurrentLocation(json.getString("currentLocation"));
        tracking.setOrder_id(json.getJsonNumber("order_id").longValue());
        tracking.setDelivery_partner_id(json.getJsonNumber("delivery_partner_id").longValue());
        return tracking;
    }

    
}
