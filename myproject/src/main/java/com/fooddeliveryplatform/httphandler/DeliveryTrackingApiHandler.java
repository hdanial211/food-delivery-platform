package com.fooddeliveryplatform.httphandler;
import com.fooddeliveryplatform.dao.DeliveryTrackingDao;
import com.fooddeliveryplatform.model.DeliveryTracking;

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

public class DeliveryTrackingApiHandler implements HttpHandler {

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
        // Logic to handle GET request for delivery tracking
        DeliveryTrackingDao deliveryTrackingDao = new DeliveryTrackingDao();
        List<DeliveryTracking> deliveryTrackings = deliveryTrackingDao.getAll();
        javax.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (DeliveryTracking tracking : deliveryTrackings) {
            arrayBuilder.add(tracking.toJson());
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
        DeliveryTracking deliveryTracking = DeliveryTracking.fromJson(json);
        DeliveryTrackingDao deliveryTrackingDao = new DeliveryTrackingDao();
        DeliveryTracking savedTracking = deliveryTrackingDao.save(deliveryTracking);

        if (savedTracking != null) {
            sendJsonResponse(exchange, 201, savedTracking.toJson().toString());
        } else {
            sendResponse(exchange, 400, "Failed to save delivery tracking");
        }
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // Implement other methods like handleGet, handlePost, etc.
    
}
