package org.example.dao;

import org.example.config.DatabaseConnection;
import org.example.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) class for the Student model.
 * Performs database CRUD operations and handles table creation and schema migration.
 */
public class StudentDAO {

    // Logger instance for logging database access events and errors
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);

    /**
     * Constructor that automatically initializes the database table if it doesn't exist.
     */
    public StudentDAO() {
        createTableIfNotExists();
    }

    /**
     * Creates the 'students' table in the database if it does not already exist.
     * Drops the old table if a schema mismatch (legacy 'cgpa' column) is detected.
     * SQL query: CREATE TABLE IF NOT EXISTS students (...)
     */
    public void createTableIfNotExists() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            var meta = conn.getMetaData();
            boolean tableExists = false;
            boolean hasCgpa = false;
            boolean hasAddress = false;

            // Check if table exists and inspect columns
            try (ResultSet rs = meta.getColumns(null, null, "students", null)) {
                while (rs.next()) {
                    tableExists = true;
                    String columnName = rs.getString("COLUMN_NAME");
                    if ("cgpa".equalsIgnoreCase(columnName)) {
                        hasCgpa = true;
                    }
                    if ("address".equalsIgnoreCase(columnName)) {
                        hasAddress = true;
                    }
                }
            }

            // Fallback for uppercase/mixedcase catalog lookup (PostgreSQL is usually lowercase)
            if (!tableExists) {
                try (ResultSet rs = meta.getColumns(null, null, "STUDENTS", null)) {
                    while (rs.next()) {
                        tableExists = true;
                        String columnName = rs.getString("COLUMN_NAME");
                        if ("cgpa".equalsIgnoreCase(columnName)) {
                            hasCgpa = true;
                        }
                        if ("address".equalsIgnoreCase(columnName)) {
                            hasAddress = true;
                        }
                    }
                }
            }

            // Drop table if legacy column exists or if we are missing 'address' to force migration
            if (tableExists && (hasCgpa || !hasAddress)) {
                logger.info("Outdated schema detected (has cgpa or missing address). Dropping table 'students' to force recreate.");
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("DROP TABLE IF EXISTS students CASCADE");
                }
            }
        } catch (SQLException e) {
            // Log connection warning but continue table setup logic
            logger.warn("Legacy schema detection check failed: {}", e.getMessage());
        }

        // DDL Statement to create the table structure
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "age INT NOT NULL, " +
                "department VARCHAR(100) NOT NULL, " +
                "role VARCHAR(100) NOT NULL, " +
                "address VARCHAR(100) NOT NULL" +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Execute the table creation SQL query
            stmt.execute(createTableSQL);
            logger.info("Database table check completed. Table 'students' is verified.");
        } catch (SQLException e) {
            logger.error("Error creating students table: {}", e.getMessage(), e);
        }
    }

    /**
     * Inserts a new student record into the 'students' table.
     * SQL query: INSERT INTO students (name, age, department, role, address) VALUES (?, ?, ?, ?, ?)
     * 
     * @param student the student entity to insert
     * @return true if insertion was successful, false otherwise
     */
    public boolean addStudent(Student student) {
        String insertSQL = "INSERT INTO students (name, age, department, role, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set query parameters
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getDepartment());
            pstmt.setString(4, student.getRole());
            pstmt.setString(5, student.getAddress());

            // Run the insert query
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Retrieve auto-generated primary key (ID)
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                    }
                }
                logger.info("Student Added: ID: {}, Name: {}", student.getId(), student.getName());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error adding student record: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * Retrieves all student records from the 'students' table.
     * SQL query: SELECT id, name, age, department, role, address FROM students ORDER BY id
     * 
     * @return a List of all students in the database
     */
    public List<Student> getAllStudents() {
        List<Student> studentsList = new ArrayList<>();
        String selectAllSQL = "SELECT id, name, age, department, role, address FROM students ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSQL)) {
            
            // Loop through result set and construct Student objects
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("department"),
                        rs.getString("role"),
                        rs.getString("address")
                );
                studentsList.add(student);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all students: {}", e.getMessage(), e);
        }
        return studentsList;
    }

    /**
     * Retrieves a single student record by their unique ID.
     * SQL query: SELECT id, name, age, department, role, address FROM students WHERE id = ?
     * 
     * @param id the unique ID of the student
     * @return an Optional container holding the student if found, or empty otherwise
     */
    public Optional<Student> getStudentById(int id) {
        String selectSQL = "SELECT id, name, age, department, role, address FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            
            // Bind the student ID to the query
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getString("role"),
                            rs.getString("address")
                    );
                    return Optional.of(student);
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching student with ID {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Updates an existing student record.
     * SQL query: UPDATE students SET name = ?, age = ?, department = ?, role = ?, address = ? WHERE id = ?
     * 
     * @param student the student entity with updated values
     * @return true if the student record was successfully updated, false otherwise
     */
    public boolean updateStudent(Student student) {
        String updateSQL = "UPDATE students SET name = ?, age = ?, department = ?, role = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            
            // Bind updated properties
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getDepartment());
            pstmt.setString(4, student.getRole());
            pstmt.setString(5, student.getAddress());
            pstmt.setInt(6, student.getId());

            // Execute the update command
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Student Updated: ID: {}, Name: {}", student.getId(), student.getName());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating student with ID {}: {}", student.getId(), e.getMessage(), e);
        }
        return false;
    }

    /**
     * Deletes a student record by their ID.
     * SQL query: DELETE FROM students WHERE id = ?
     * 
     * @param id the unique ID of the student to delete
     * @return true if a student record was successfully deleted, false otherwise
     */
    public boolean deleteStudent(int id) {
        String deleteSQL = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            
            // Bind the parameter
            pstmt.setInt(1, id);

            // Execute delete statement
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Student Deleted: ID: {}", id);

                // Reset the auto-increment primary key sequence to avoid gaps (e.g. from test inserts/deletions)
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("SELECT setval('students_id_seq', COALESCE((SELECT MAX(id) FROM students), 0) + 1, false)");
                } catch (SQLException seqEx) {
                    // Log sequence reset warning but do not fail the overall deletion operation
                    logger.debug("Sequence reset warning: {}", seqEx.getMessage());
                }

                return true;
            }
        } catch (SQLException e) {
            logger.error("Error deleting student with ID {}: {}", id, e.getMessage(), e);
        }
        return false;
    }
}
