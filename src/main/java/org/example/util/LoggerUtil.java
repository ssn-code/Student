package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to centralize and retrieve SLF4J logger instances.
 * Simplifies and standardizes logging behavior across packages.
 */
public class LoggerUtil {

    /**
     * Private constructor to prevent initialization of utility class.
     */
    private LoggerUtil() {
        // Prevent instantiation
    }

    /**
     * Factory method retrieving a Logger configured for a specific class context.
     * 
     * @param clazz the class requesting the logger
     * @return configured SLF4J Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Convenience method to log standard application flow messages.
     * 
     * @param logger the source logger
     * @param message the message to log
     */
    public static void logInfo(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * Convenience method to log standard application errors.
     * 
     * @param logger the source logger
     * @param errorMsg the error message
     * @param throwable root cause exception
     */
    public static void logError(Logger logger, String errorMsg, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error("Errors: {} - Reason: {}", errorMsg, throwable.getMessage(), throwable);
        }
    }
}
