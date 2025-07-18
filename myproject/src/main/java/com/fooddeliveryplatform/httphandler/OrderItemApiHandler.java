package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.dao.OrderItemDao;
import com.fooddeliveryplatform.model.OrderItem;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

public class OrderItemApiHandler implements HttpHandler {

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
        OrderItemDao orderItemDao = new OrderItemDao();
        String path = exchange.getRequestURI().getPath();
        
        if (path.matches("/api/order-items/\\d+")) {
            // Handle GET /api/order-items/{id}
            String[] parts = path.split("/");
            long orderItemId = Long.parseLong(parts[3]);
            OrderItem orderItem = orderItemDao.get((int) orderItemId);
            
            if (orderItem != null) {
                String jsonResponse = orderItem.toJson().toString();
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "Order item not found");
            }
        } else if (path.matches("/api/orders/\\d+/items")) {
            // Handle GET /api/orders/{orderId}/items
            String[] parts = path.split("/");
            long orderId = Long.parseLong(parts[3]);
            
            List<OrderItem> orderItems = orderItemDao.getAll().stream()
                .filter(item -> item.getOrderId() == orderId)
                .toList();
                
            javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (OrderItem item : orderItems) {
                arrayBuilder.add(item.toJson());
            }
            String jsonResponse = arrayBuilder.build().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            // Handle GET /api/order-items
            List<OrderItem> orderItems = orderItemDao.getAll();
            javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (OrderItem item : orderItems) {
                arrayBuilder.add(item.toJson());
            }
            String jsonResponse = arrayBuilder.build().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        OrderItem orderItem = OrderItem.fromJson(jsonRequest);
        OrderItemDao orderItemDao = new OrderItemDao();
        OrderItem savedOrderItem = orderItemDao.save(orderItem);
        
        if (savedOrderItem != null) {
            String jsonResponse = savedOrderItem.toJson().toString();
            sendJsonResponse(exchange, 201, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to create order item");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/order-items/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long orderItemId = Long.parseLong(parts[3]);
        
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        OrderItemDao orderItemDao = new OrderItemDao();
        OrderItem existingItem = orderItemDao.get((int) orderItemId);
        
        if (existingItem == null) {
            sendResponse(exchange, 404, "Order item not found");
            return;
        }
        
        // Update order item fields from request
        existingItem.setQuantity(jsonRequest.getInt("quantity"));
        existingItem.setSpecialInstructions(jsonRequest.getString("specialInstructions", null));
        // Add other fields as needed
        
        OrderItem updatedItem = orderItemDao.update(existingItem);
        
        if (updatedItem != null) {
            String jsonResponse = updatedItem.toJson().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to update order item");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/order-items/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long orderItemId = Long.parseLong(parts[3]);
        
        OrderItemDao orderItemDao = new OrderItemDao();
        OrderItem orderItem = orderItemDao.get((int) orderItemId);
        
        if (orderItem == null) {
            sendResponse(exchange, 404, "Order item not found");
            return;
        }
        
        orderItemDao.delete(orderItem);
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