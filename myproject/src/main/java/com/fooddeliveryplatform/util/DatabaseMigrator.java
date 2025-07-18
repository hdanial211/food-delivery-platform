package com.fooddeliveryplatform.util;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fooddeliveryplatform.config.AppConfig;

public class DatabaseMigrator {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrator.class);

    public static void migrate() {
        logger.info("Starting database migration");
        
        Flyway flyway = Flyway.configure()
            .dataSource(
                AppConfig.getDatabaseUrl(),
                AppConfig.getDatabaseUsername(),
                AppConfig.getDatabasePassword()
            )
            .locations(AppConfig.getMigrationLocations())
            .load();
        
        flyway.migrate();
        logger.info("Database migration completed");
    }
}
