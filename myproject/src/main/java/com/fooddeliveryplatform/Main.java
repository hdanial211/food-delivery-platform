package com.fooddeliveryplatform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fooddeliveryplatform.config.AppConfig;
import com.fooddeliveryplatform.server.MyServer;
import com.fooddeliveryplatform.util.DatabaseMigrator;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static MyServer server;
    public static void main(String[] args) {
        try {
            logger.info("Starting application initialization");
            // Run database migrations
            DatabaseMigrator.migrate();
            // Start HTTP server
            server = new MyServer(AppConfig.getServerPort());
            server.start();
            logger.info("Application started successfully on port {}", AppConfig.getServerPort());
        } catch (Exception e) {
            logger.error("Application failed to start", e);
            System.exit(1);
        }
    }
}