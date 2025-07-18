package com.fooddeliveryplatform.dao;

import com.fooddeliveryplatform.model.Order;
import com.fooddeliveryplatform.model.OrderStatus;
import com.fooddeliveryplatform.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements Dao<Order> {

    @Override
    public Order get(int id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getLong("order_id"),
                        rs.getDate("created_at"),
                        rs.getString("delivery_address"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getDouble("total_amount"),
                        rs.getLong("customer_id"),
                        rs.getLong("restaurant_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getAll() throws SQLException {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getLong("order_id"),
                        rs.getDate("created_at"),
                        rs.getString("delivery_address"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getDouble("total_amount"),
                        rs.getLong("customer_id"),
                        rs.getLong("restaurant_id")
                ));
            }
            return orders;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Order save(Order order) {
        String sql = "INSERT INTO orders (created_at, delivery_address, status, total_amount, customer_id, restaurant_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, new Date(order.getCreatedAt().getTime()));
            pstmt.setString(2, order.getDeliveryAddress());
            pstmt.setString(3, order.getStatus().name());
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setLong(5, order.getCustomerId());
            pstmt.setLong(6, order.getRestaurantId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderId(generatedKeys.getLong(1));
                        return order;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order update(Order order) {
        String sql = "UPDATE orders SET delivery_address = ?, status = ?, total_amount = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getDeliveryAddress());
            pstmt.setString(2, order.getStatus().name());
            pstmt.setDouble(3, order.getTotalAmount());
            pstmt.setLong(4, order.getOrderId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Order order) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, order.getOrderId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByCustomerId(long customerId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getLong("order_id"),
                        rs.getDate("created_at"),
                        rs.getString("delivery_address"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getDouble("total_amount"),
                        rs.getLong("customer_id"),
                        rs.getLong("restaurant_id")
                ));
            }
        }
        return orders;
    }

    public List<Order> getOrdersByRestaurantId(long restaurantId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE restaurant_id = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getLong("order_id"),
                        rs.getDate("created_at"),
                        rs.getString("delivery_address"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getDouble("total_amount"),
                        rs.getLong("customer_id"),
                        rs.getLong("restaurant_id")
                ));
            }
        }
        return orders;
    }
}