package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.dao.OrderDao;
import com.fooddeliveryplatform.model.Order;
import com.fooddeliveryplatform.model.OrderStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

public class OrderApiHandler implements HttpHandler {

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
                case "DELETE":
                    handleDelete(exchange);
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
        OrderDao orderDao = new OrderDao();
        String path = exchange.getRequestURI().getPath();
        
        if (path.matches("/api/orders/\\d+")) {
            // Handle GET /api/orders/{id}
            String[] parts = path.split("/");
            long orderId = Long.parseLong(parts[3]);
            Order order = orderDao.get((int) orderId);
            
            if (order != null) {
                String jsonResponse = order.toJson().toString();
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "Order not found");
            }
        } else {
            // Handle GET /api/orders
            List<Order> orders = orderDao.getAll();
            javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Order order : orders) {
                arrayBuilder.add(order.toJson());
            }
            String jsonResponse = arrayBuilder.build().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        Order order = Order.fromJson(jsonRequest);
        OrderDao orderDao = new OrderDao();
        Order savedOrder = orderDao.save(order);
        
        if (savedOrder != null) {
            String jsonResponse = savedOrder.toJson().toString();
            sendJsonResponse(exchange, 201, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to create order");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/orders/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long orderId = Long.parseLong(parts[3]);
        
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        OrderDao orderDao = new OrderDao();
        Order existingOrder = orderDao.get((int) orderId);
        
        if (existingOrder == null) {
            sendResponse(exchange, 404, "Order not found");
            return;
        }
        
        // Update order fields from request
        existingOrder.setStatus(OrderStatus.valueOf(jsonRequest.getString("status")));
        existingOrder.setDeliveryAddress(jsonRequest.getString("deliveryAddress"));
        // Add other fields as needed
        
        Order updatedOrder = orderDao.update(existingOrder);
        
        if (updatedOrder != null) {
            String jsonResponse = updatedOrder.toJson().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to update order");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/orders/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long orderId = Long.parseLong(parts[3]);
        
        OrderDao orderDao = new OrderDao();
        Order order = orderDao.get((int) orderId);
        
        if (order == null) {
            sendResponse(exchange, 404, "Order not found");
            return;
        }
        
        orderDao.delete(order);
        sendResponse(exchange, 204, "");
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