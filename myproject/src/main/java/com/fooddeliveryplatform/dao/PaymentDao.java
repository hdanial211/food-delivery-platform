package com.fooddeliveryplatform.dao;

import com.fooddeliveryplatform.model.Payment;
import com.fooddeliveryplatform.model.PaymentMethod;
import com.fooddeliveryplatform.model.PaymentStatus;
import com.fooddeliveryplatform.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao implements Dao<Payment> {

    @Override
    public Payment get(int id) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Payment(
                        rs.getLong("payment_id"),
                        rs.getDouble("amount"),
                        rs.getDate("created_at"),
                        PaymentMethod.valueOf(rs.getString("method")),
                        PaymentStatus.valueOf(rs.getString("status")),
                        rs.getString("transaction_id"),
                        rs.getLong("order_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> getAll() throws SQLException {
        String sql = "SELECT * FROM payments";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getLong("payment_id"),
                        rs.getDouble("amount"),
                        rs.getDate("created_at"),
                        PaymentMethod.valueOf(rs.getString("method")),
                        PaymentStatus.valueOf(rs.getString("status")),
                        rs.getString("transaction_id"),
                        rs.getLong("order_id")
                ));
            }
            return payments;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Payment save(Payment payment) {
        String sql = "INSERT INTO payments (amount, created_at, method, status, transaction_id, order_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, payment.getAmount());
            pstmt.setDate(2, new java.sql.Date(payment.getCreatedAt().getTime()));
            pstmt.setString(3, payment.getPaymentMethod().name());
            pstmt.setString(4, payment.getStatus().name());
            pstmt.setString(5, payment.getTransactionId());
            pstmt.setLong(6, payment.getOrderId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setPaymentId(generatedKeys.getLong(1));
                        return payment;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Payment update(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, method = ?, status = ?, transaction_id = ? WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, payment.getAmount());
            pstmt.setString(2, payment.getPaymentMethod().name());
            pstmt.setString(3, payment.getStatus().name());
            pstmt.setString(4, payment.getTransactionId());
            pstmt.setLong(5, payment.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return payment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Payment payment) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, payment.getPaymentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Payment getPaymentByOrderId(long orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Payment(
                        rs.getLong("payment_id"),
                        rs.getDouble("amount"),
                        rs.getDate("created_at"),
                        PaymentMethod.valueOf(rs.getString("method")),
                        PaymentStatus.valueOf(rs.getString("status")),
                        rs.getString("transaction_id"),
                        rs.getLong("order_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}