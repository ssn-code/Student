package org.example.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuration class responsible for establishing a connection to the PostgreSQL database.
 * It reads configuration properties from the environment (.env file) and manages connection creation.
 */
public class DatabaseConnection {

    // Central Logger instance for tracking database connectivity events
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    // Cached connection URL read from dotenv
    private static String databaseUrl;

    static {
        try {
            // Load environment variables from .env file
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            
            // Retrieve the DATABASE_URL property
            databaseUrl = dotenv.get("DATABASE_URL");

            // If .env does not contain the URL, fallback to system environment variables
            if (databaseUrl == null || databaseUrl.trim().isEmpty()) {
                databaseUrl = System.getenv("DATABASE_URL");
            }

            if (databaseUrl == null || databaseUrl.trim().isEmpty()) {
                logger.error("DATABASE_URL is not set in the .env file or system environment variables.");
            } else {
                logger.info("Database URL loaded successfully from environment configurations.");
            }
        } catch (Exception e) {
            logger.error("Failed to load environment variables from .env file: {}", e.getMessage(), e);
        }
    }

    /**
     * Obtains a connection to the PostgreSQL database.
     * 
     * @return a valid database Connection object, or null if connection fails.
     * @throws SQLException if a database access error occurs or the URL is invalid.
     */
    public static Connection getConnection() throws SQLException {
        if (databaseUrl == null || databaseUrl.trim().isEmpty()) {
            logger.error("Connection Failed: DATABASE_URL is null or empty.");
            throw new SQLException("DATABASE_URL environment variable is missing.");
        }

        try {
            // Explicitly load the PostgreSQL JDBC driver class
            Class.forName("org.postgresql.Driver");

            // Attempt to establish database connection
            Connection connection = DriverManager.getConnection(databaseUrl);
            
            // Log successful connection
            logger.info("Connection Success: Connected to the database.");
            return connection;
        } catch (ClassNotFoundException e) {
            logger.error("Connection Failed: PostgreSQL JDBC Driver not found.", e);
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            // Log failure status and rethrow for caller handling
            logger.error("Connection Failed: Unable to connect to the database at {}. Reason: {}", databaseUrl, e.getMessage());
            throw e;
        }
    }
}
