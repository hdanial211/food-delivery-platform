package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.dao.UserDao;
import com.fooddeliveryplatform.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

public class UserApiHandler implements HttpHandler {

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
        UserDao userDao = new UserDao();
        String path = exchange.getRequestURI().getPath();
        
        if (path.matches("/api/users/\\d+")) {
            // Handle GET /api/users/{id}
            String[] parts = path.split("/");
            long userId = Long.parseLong(parts[3]);
            User user = userDao.get((int) userId);
            
            if (user != null) {
                String jsonResponse = user.toJson().toString();
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "User not found");
            }
        } else {
            // Handle GET /api/users
            List<User> users = userDao.getAll();
            javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (User user : users) {
                arrayBuilder.add(user.toJson());
            }
            String jsonResponse = arrayBuilder.build().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        User user = User.fromJson(jsonRequest);
        UserDao userDao = new UserDao();
        User savedUser = userDao.save(user);
        
        if (savedUser != null) {
            String jsonResponse = savedUser.toJson().toString();
            sendJsonResponse(exchange, 201, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to create user");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/users/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long userId = Long.parseLong(parts[3]);
        
        InputStream is = exchange.getRequestBody();
        String requestBody = readRequestBody(is);
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        UserDao userDao = new UserDao();
        User existingUser = userDao.get((int) userId);
        
        if (existingUser == null) {
            sendResponse(exchange, 404, "User not found");
            return;
        }
        
        // Update user fields from request
        existingUser.setUsername(jsonRequest.getString("username", existingUser.getUsername()));
        existingUser.setEmail(jsonRequest.getString("email", existingUser.getEmail()));
        existingUser.setFullName(jsonRequest.getString("fullName", existingUser.getFullName()));
        existingUser.setPhone(jsonRequest.getString("phone", existingUser.getPhone()));
        existingUser.setAddress(jsonRequest.getString("address", existingUser.getAddress()));
        existingUser.setActive(jsonRequest.getBoolean("isActive", existingUser.isActive()));
        
        // Only update password if provided
        if (jsonRequest.containsKey("password")) {
            existingUser.setPassword(jsonRequest.getString("password"));
        }
        
        User updatedUser = userDao.update(existingUser);
        
        if (updatedUser != null) {
            String jsonResponse = updatedUser.toJson().toString();
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendResponse(exchange, 400, "Failed to update user");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        if (!path.matches("/api/users/\\d+")) {
            sendResponse(exchange, 400, "Invalid request");
            return;
        }

        String[] parts = path.split("/");
        long userId = Long.parseLong(parts[3]);
        
        UserDao userDao = new UserDao();
        User user = userDao.get((int) userId);
        
        if (user == null) {
            sendResponse(exchange, 404, "User not found");
            return;
        }
        
        userDao.delete(user);
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