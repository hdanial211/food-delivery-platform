package com.fooddeliveryplatform.model;

import java.sql.Date;

import javax.json.Json;
import javax.json.JsonObject;

public class Payment {
    private Long paymentId;
    private double amount;
    private Date createdAt;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private Long orderId;
    public Payment(Long paymentId, double amount, Date createdAt, PaymentMethod paymentMethod, PaymentStatus status,
            String transactionId, Long orderId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
        this.orderId = orderId;
    }
    
    public Payment(double amount, Date createdAt, PaymentMethod paymentMethod, PaymentStatus status,
            String transactionId, Long orderId) {
        this.amount = amount;
        this.createdAt = createdAt;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public PaymentStatus getStatus() {
        return status;
    }
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public static Payment fromJson(JsonObject json) {
        double amount = json.getJsonNumber("amount").doubleValue();
        Date createdAt = Date.valueOf(json.getString("createdAt"));
        PaymentMethod paymentMethod = PaymentMethod.valueOf(json.getString("paymentMethod"));
        PaymentStatus status = PaymentStatus.valueOf(json.getString("status"));
        String transactionId = json.getString("transactionId");
        Long orderId = json.getJsonNumber("orderId").longValue();

        return new Payment(amount, createdAt, paymentMethod, status, transactionId, orderId);
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("paymentId", paymentId)
            .add("amount", amount)
            .add("createdAt", createdAt.toString())
            .add("paymentMethod", paymentMethod.name())
            .add("status", status.name())
            .add("transactionId", transactionId)
            .add("orderId", orderId)
            .build();
    }
}
