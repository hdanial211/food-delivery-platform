package com.fooddeliveryplatform.util;

import com.fooddeliveryplatform.config.AppConfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import java.util.Date;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
        AppConfig.getJwtSecret().getBytes());
    private static final long EXPIRATION_MS = AppConfig.getJwtExpirationMs();

    public static String generateToken(String username, String role) {
        return Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(SECRET_KEY)
            .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}