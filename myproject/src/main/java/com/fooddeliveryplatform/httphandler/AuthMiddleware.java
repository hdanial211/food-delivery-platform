package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.util.JwtUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class AuthMiddleware implements HttpHandler {
    private final HttpHandler next;
    private final List<String> allowedRoles;

    public AuthMiddleware(HttpHandler next, String... allowedRoles) {
        this.next = next;
        this.allowedRoles = Arrays.asList(allowedRoles);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendResponse(exchange, 401, "Unauthorized");
            return;
        }

        String token = authHeader.substring(7);
        if (!JwtUtil.validateToken(token)) {
            sendResponse(exchange, 401, "Invalid Token");
            return;
        }
        try {
                Claims claims = JwtUtil.parseToken(token);
                String role = claims.get("role", String.class);
                System.out.println("Authenticated user: " + claims.getSubject() + " with role: " + role);
                if (!allowedRoles.isEmpty() && !allowedRoles.contains(role)) {
                    sendResponse(exchange, 403, "Insufficient permissions");
                    return;
                }
                
                // Add user info to request attributes
                exchange.setAttribute("username", claims.getSubject());
                exchange.setAttribute("role", role);
                System.out.println("User " + claims.getSubject() + " with role " + role + " is authorized to access this resource.");
                // Proceed to the next handler
                next.handle(exchange);
        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
}
