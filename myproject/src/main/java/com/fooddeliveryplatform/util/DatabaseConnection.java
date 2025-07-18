package com.fooddeliveryplatform.util;

import com.fooddeliveryplatform.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            AppConfig.getDatabaseUrl(),
            AppConfig.getDatabaseUsername(),
            AppConfig.getDatabasePassword()
        );
    }
}
