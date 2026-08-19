package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.example.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service class responsible for exporting student records into a JSON file format.
 * Utilizes Gson for JSON serialization and Apache Commons IO for file writing.
 */
public class JsonService {

    // Logger instance for tracking the JSON export process
    private static final Logger logger = LoggerFactory.getLogger(JsonService.class);

    // Target file path where students will be exported
    private static final String EXPORT_FILE_NAME = "students.json";

    // Reusable, thread-safe Gson instance configured for pretty printing
    private final Gson gson;

    /**
     * Constructor initializing the Gson parser with pretty printing enabled.
     */
    public JsonService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Exports a list of student records into the 'students.json' file in the root directory.
     * Uses FileUtils.writeStringToFile to securely persist JSON content.
     * 
     * @param students the list of students to export
     * @return true if export succeeds, false if an IOException is thrown
     */
    public boolean exportToJson(List<Student> students) {
        try {
            // Serialize list of students into pretty-printed JSON structure
            String jsonOutput = gson.toJson(students);

            // Create target file pointer
            File file = new File(EXPORT_FILE_NAME);

            // Write JSON string representation to target file using Apache Commons IO FileUtils
            FileUtils.writeStringToFile(file, jsonOutput, StandardCharsets.UTF_8);

            // Log successful export operation
            logger.info("Export Completed: Serialized {} students to {}", students.size(), EXPORT_FILE_NAME);
            return true;
        } catch (IOException e) {
            // Log file writing errors
            logger.error("Errors: Failed to write student records to JSON file: {}", e.getMessage(), e);
            return false;
        }
    }
}
