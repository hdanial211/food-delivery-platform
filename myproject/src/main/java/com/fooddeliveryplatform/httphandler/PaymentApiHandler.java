package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.dao.PaymentDao;
import com.fooddeliveryplatform.model.Payment;
import com.fooddeliveryplatform.model.PaymentStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

public class PaymentApiHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "PUT":
                    handlePut(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException, SQLException {
        PaymentDao paymentDao = new PaymentDao();
        String path = exchange.getRequestURI().getPath();
        
        if (path.matches("/api/payments/\\d+")) {
            // Handle GET /api/payments/{id}
            String[] parts = path.split("/");
            long paymentId = Long.parseLong(parts[3]);
            Payment payment = paymentDao.get((int) paymentId);
            
            if (payment != null) {
                String jsonResponse = payment.toJson().toString();
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "Payment not found");
            }
        } else if (path.matches("/api/orders/\\d+/payment")) {
            // Handle GET /api/orders/{orderId}/payment
            String[] parts = path.split("/");
            long orderId = Long.parseLong(parts[3]);
            
            List<Payment> payments = paymentDao.getAll().stream()
                .filter(p -> p.getOrderId() == orderId)
                .toList();
                
            if (!payments.isEmpty()) {
                String jsonResponse = payments.get(0).toJson().toString();
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "Payment not found for this order");
            }
        } else {
            // Handle GET /api/payments
            List<Payment> payments = paymentDao.getAll();
            javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Payment payment : payments) {
                arrayBuilder.add(payment.toJson());
            }
            String jsonResponse = arrayBuilder.build().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        Payment payment = Payment.fromJson(jsonRequest);
        PaymentDao paymentDao = new PaymentDao();
        Payment savedPayment = paymentDao.save(payment);
        
        if (savedPayment != null) {
            String jsonResponse = savedPayment.toJson().toString();
            sendJsonResponse(exchange, 201, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to process payment");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/payments/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long paymentId = Long.parseLong(parts[3]);
        
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        PaymentDao paymentDao = new PaymentDao();
        Payment existingPayment = paymentDao.get((int) paymentId);
        
        if (existingPayment == null) {
            sendResponse(exchange, 404, "Payment not found");
            return;
        }
        
        // Update payment fields from request
        existingPayment.setStatus(PaymentStatus.valueOf(jsonRequest.getString("status")));
        existingPayment.setTransactionId(jsonRequest.getString("transactionId"));
        // Add other fields as needed
        
        Payment updatedPayment = paymentDao.update(existingPayment);
        
        if (updatedPayment != null) {
            String jsonResponse = updatedPayment.toJson().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to update payment");
        }
    }

    private String readRequestBody(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = is.read()) != -1) {
            sb.append((char) ch);
        }
        return sb.toString();
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, statusCode, response);
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            if (!response.isEmpty()) {
                os.write(response.getBytes());
            }
        }
    }
}