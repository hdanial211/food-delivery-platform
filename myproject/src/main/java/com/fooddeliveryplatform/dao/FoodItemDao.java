package com.fooddeliveryplatform.dao;


import java.util.List;



import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.fooddeliveryplatform.model.FoodItem;
import com.fooddeliveryplatform.util.DatabaseConnection;

public class FoodItemDao implements Dao<FoodItem> {
    // Implement methods for CRUD operations on FoodItem entities
    @Override
    public FoodItem get(int id) {
        // Logic to retrieve a FoodItem by id

        String sql = "SELECT * FROM food_items WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new FoodItem(
                        rs.getLong("food_id"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_available"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getLong("restaurant_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
         // Placeholder return
    }

    @Override
    public List<FoodItem> getAll() throws SQLException {
        // Logic to retrieve all FoodItems
        String getAll = "SELECT * FROM food_items";
        List<FoodItem> foodItems = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getAll);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                foodItems.add(new FoodItem(
                        rs.getLong("food_id"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_available"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getLong("restaurant_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems; // Placeholder return
    }

    @Override
    public FoodItem save(FoodItem foodItem) {
        // Logic to save a FoodItem
        String saveToFoodItem = "INSERT INTO food_items (category, description, image_url, is_available, name, price, restaurant_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(saveToFoodItem, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, foodItem.getCategory());
                pstmt.setString(2, foodItem.getDescription());
                pstmt.setString(3, foodItem.getImageUrl());
                pstmt.setBoolean(4, foodItem.getIs_available());
                pstmt.setString(5, foodItem.getName());
                pstmt.setDouble(6, foodItem.getPrice());
                pstmt.setLong(7, foodItem.getRestaurantId());
    
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        foodItem.setFood_Id(generatedKeys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null; // Placeholder return
    }
    
    public void delete(int id) {
        // Logic to delete a FoodItem by id
        /*String deleteFromFoodItem = "DELETE FROM food_items WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(deleteFromFoodItem)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
    }

    @Override
    public void delete(FoodItem foodItem) {
        // Logic to delete a FoodItem by object
    }

    @Override
    public FoodItem update(FoodItem foodItem) {
        // Logic to update a FoodItem
        return null; // Placeholder return
    }

    public FoodItem findById(Long id) {
        // Logic to find a FoodItem by id
        String sql = "SELECT * FROM food_items WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new FoodItem(
                        rs.getLong("food_id"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_available"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getLong("restaurant_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Placeholder return
    }
    
}
