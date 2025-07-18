package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.dao.RestaurantDao;
import com.fooddeliveryplatform.model.Restaurant;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;


public class RestaurantApiHandler implements HttpHandler {
    
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
                    //handlePut(exchange);
                    break;
                case "DELETE":
                    //handleDelete(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error");
        }
    } 
    
    public void handleGet(HttpExchange exchange) throws IOException, SQLException {
        // Logic to handle GET request for restaurants
        // Example: Fetching all restaurants from the database
        RestaurantDao restaurantDao = new RestaurantDao();
        List<Restaurant> restaurants = restaurantDao.getAll();
        javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Restaurant restaurant : restaurants) {
            arrayBuilder.add(restaurant.toJson());
        }
        String jsonResponse = arrayBuilder.build().toString();
        sendJsonResponse(exchange, 200, jsonResponse);
    }
    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        InputStream is = exchange.getRequestBody();
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = is.read()) != -1) {
            sb.append((char) ch);
        }
        
        String requestBody = sb.toString();
        JsonObject jsonRequest = Json.createReader(new java.io.StringReader(requestBody)).readObject();
        
        Restaurant restaurant = Restaurant.fromJson(jsonRequest);
        RestaurantDao restaurantDao = new RestaurantDao();
        Restaurant savedRestaurant = restaurantDao.save(restaurant);
        
        if (savedRestaurant != null) {
            sendJsonResponse(exchange, 201, savedRestaurant.toJson().toString());
        } else {
            sendResponse(exchange, 400, "Failed to create restaurant");
        }
    }
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, statusCode, response);
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
