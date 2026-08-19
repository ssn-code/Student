package org.example;

import org.example.util.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point class for the Student Management System.
 * Bootstraps the database connection check, performs startup logging, and executes the CLI Menu loop.
 */
public class Main {

    // Logger instance for tracing application lifecycle milestones
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * The main method that serves as the entry point of the Java console application.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Log Application Started event
        logger.info("Application Started: Student Management System CLI initialization beginning.");

        try {
            // Instantiate Menu class which implicitly triggers StudentDAO constructor (creating tables if not exists)
            Menu menu = new Menu();

            // Run the interactive user menu loop
            menu.displayMenu();

        } catch (Exception e) {
            // Log any unexpected general exceptions
            logger.error("Errors: An unexpected error occurred during application execution: {}", e.getMessage(), e);
            System.err.println("Fatal: The application encountered an unexpected error and must terminate. See logs for details.");
        } finally {
            // Log Application Shutdown event
            logger.info("Application Stopped: Student Management System CLI shutting down.");
        }
    }
}