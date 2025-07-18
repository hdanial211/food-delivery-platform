package com.fooddeliveryplatform.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fooddeliveryplatform.model.DeliveryTracking;
import com.fooddeliveryplatform.util.DatabaseConnection;

public class DeliveryTrackingDao implements Dao<DeliveryTracking> {
    // Implement methods for CRUD operations on DeliveryTracking entities
    @Override
    public DeliveryTracking get(int id) {
        String sql = "SELECT * FROM delivery_tracking WHERE tracking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new DeliveryTracking(
                        rs.getLong("tracking_id"),
                        rs.getLong("order_id"),
                        rs.getString("status"),
                        rs.getString("estimated_delivery_time"),
                        rs.getString("current_location"),
                        rs.getLong("order_id"),
                        rs.getLong("delivery_partner_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   @Override
    public List<DeliveryTracking> getAll() throws SQLException {
        String sql = "SELECT * FROM delivery_tracking";
        List<DeliveryTracking> deliveryTrackings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                deliveryTrackings.add(new DeliveryTracking(
                        rs.getLong("tracking_id"),
                        rs.getLong("order_id"),
                        rs.getString("status"),
                        rs.getString("estimated_delivery_time"),
                        rs.getString("current_location"),
                        rs.getLong("order_id"),
                        rs.getLong("delivery_partner_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveryTrackings;
    }

    @Override
    public DeliveryTracking save(DeliveryTracking deliveryTracking) {
        // Implement save logic here
        // Example:
        // String sql = "INSERT INTO delivery_tracking (...) VALUES (?, ?, ...)";
        return null;
    }

    @Override
    public DeliveryTracking update(DeliveryTracking deliveryTracking) {
        // Implement update logic here
        // Example:
        // String sql = "UPDATE delivery_tracking SET ... WHERE tracking_id = ?";
        return null;
    }

    @Override
    public void delete(DeliveryTracking deliveryTracking) {
        // Implement delete logic here
        // Example:
        // String sql = "DELETE FROM delivery_tracking WHERE tracking_id = ?";
    }

    
}
