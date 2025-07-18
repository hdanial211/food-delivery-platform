package com.fooddeliveryplatform.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fooddeliveryplatform.model.Restaurant;
import com.fooddeliveryplatform.model.User;
import com.fooddeliveryplatform.util.DatabaseConnection;

public class RestaurantDao implements Dao<Restaurant> {
    // Implement methods for CRUD operations on Restaurant entities
    @Override
    public Restaurant get(int id) {
        // Logic to retrieve a Restaurant by id
        return null; // Placeholder return
    }

    @Override
    public List<Restaurant> getAll() throws SQLException {
        UserDao userDao = new UserDao();
        String string = "SELECT * FROM restaurants";
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(string)) {
            
            while (rs.next()) {
                restaurants.add(new Restaurant(
                        rs.getLong("restaurant_id"),
                        rs.getString("closing_time"),
                        rs.getInt("delivery_radius"),
                        rs.getString("description"),
                        rs.getString("logo_url"),
                        rs.getString("name"),
                        rs.getString("opening_time"),
                        rs.getLong("user_id"), // Create User object with owner_id
                        rs.getBoolean("is_active")
                ));
            }
            for(Restaurant restaurant : restaurants) {
                
                User owner = userDao.findById(restaurant.getOwnerId());
                restaurant.setOwner(owner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (closing_time, delivery_radius, description, logo_url, name, opening_time, user_id, is_active) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, restaurant.getClosingTime());
            pstmt.setInt(2, restaurant.getDeliveryRadius());
            pstmt.setString(3, restaurant.getDescription());
            pstmt.setString(4, restaurant.getLogoUrl());
            pstmt.setString(5, restaurant.getName());
            pstmt.setString(6, restaurant.getOpeningTime());
            pstmt.setLong(7, restaurant.getOwner().getUserId());
            pstmt.setBoolean(8, restaurant.isActive());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        restaurant.setId(generatedKeys.getLong(1));
                        return restaurant;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Placeholder return
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        // Logic to update an existing Restaurant
        return null; // Placeholder return
    }

    @Override
    public void delete(Restaurant restaurant) {
        // Logic to delete a Restaurant
    }
    
    public Restaurant findOwnerById(Long ownerId) {
        String sql = "SELECT * FROM restaurants WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Restaurant(
                        rs.getLong("restaurant_id"),
                        rs.getString("closing_time"),
                        rs.getInt("delivery_radius"),
                        rs.getString("description"),
                        rs.getString("logo_url"),
                        rs.getString("name"),
                        rs.getString("opening_time"),
                        rs.getLong("user_id"), // Assuming owner is set later or not required in constructor
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Placeholder return
    }
}
