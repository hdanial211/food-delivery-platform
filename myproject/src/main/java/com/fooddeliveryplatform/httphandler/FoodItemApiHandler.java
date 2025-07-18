package com.fooddeliveryplatform.httphandler;
import com.fooddeliveryplatform.model.FoodItem;
import com.fooddeliveryplatform.dao.FoodItemDao;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

public class FoodItemApiHandler implements HttpHandler {
    
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
                    // handlePut(exchange);
                    break;
                case "DELETE":
                    // handleDelete(exchange);
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
        // Logic to handle GET request for food items
        // Example: Fetching all food items from the database
        FoodItemDao foodItemDao = new FoodItemDao();
        List<FoodItem> foodItems = foodItemDao.getAll();
        javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (FoodItem foodItem : foodItems) {
            arrayBuilder.add(foodItem.toJson());
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
        
        JsonObject json = Json.createReader(new StringReader(sb.toString())).readObject();
        FoodItem foodItem = FoodItem.fromJson(json);
        FoodItemDao foodItemDao = new FoodItemDao();
        foodItemDao.save(foodItem);
        
        if (foodItem != null) {
            sendJsonResponse(exchange, 201, foodItem.toJson().toString());
        } else {
            sendResponse(exchange, 400, "Failed to save food item");
        }
        
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, statusCode, response);
    }



    
}
