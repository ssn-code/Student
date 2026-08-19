package org.example.service;

import org.example.dao.StudentDAO;
import org.example.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service class that coordinates Student CRUD operations, business logic,
 * validation processes, and file exports.
 */
public class StudentService {

    // Logger instance for recording service events
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    // Data Access Object layer dependency
    private final StudentDAO studentDAO;

    // JSON export service dependency
    private final JsonService jsonService;

    /**
     * Constructor initializing the required DAO and service layers.
     */
    public StudentService() {
        this.studentDAO = new StudentDAO();
        this.jsonService = new JsonService();
    }

    /**
     * Validates and registers a new student record.
     * 
     * @param name       the student's name
     * @param age        the student's age
     * @param department the student's department
     * @param role       the student's role
     * @param address    the student's address
     * @return true if insertion was successful, false if inputs were invalid or execution failed
     */
    public boolean addStudent(String name, int age, String department, String role, String address) {
        // Validate student attributes using ValidationService
        if (!ValidationService.isValidName(name)) {
            logger.error("Errors: Add Student failed due to invalid name: '{}'", name);
            return false;
        }
        if (!ValidationService.isValidAge(age)) {
            logger.error("Errors: Add Student failed due to invalid age: {}", age);
            return false;
        }
        if (!ValidationService.isValidDepartment(department)) {
            logger.error("Errors: Add Student failed due to invalid department: '{}'", department);
            return false;
        }
        if (!ValidationService.isValidRole(role)) {
            logger.error("Errors: Add Student failed due to invalid role: '{}'", role);
            return false;
        }
        if (!ValidationService.isValidAddress(address)) {
            logger.error("Errors: Add Student failed due to invalid address: '{}'", address);
            return false;
        }

        // Construct Student model
        Student student = new Student(name, age, department, role, address);

        // Save student record through DAO
        boolean isSuccess = studentDAO.addStudent(student);
        if (isSuccess) {
            logger.info("Student registration service success. ID: {}", student.getId());
        }
        return isSuccess;
    }

    /**
     * Retrieves all student records.
     * 
     * @return a List of all students
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    /**
     * Searches for a student by their ID.
     * 
     * @param id the student's ID
     * @return an Optional of Student
     */
    public Optional<Student> getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    /**
     * Validates and updates all fields of a student record.
     * 
     * @param id         the unique ID of the student to update
     * @param name       the updated name
     * @param age        the updated age
     * @param department the updated department
     * @param role       the updated role
     * @param address    the updated address
     * @return true if the record was updated, false otherwise
     */
    public boolean updateStudent(int id, String name, int age, String department, String role, String address) {
        // Ensure student exists before updating
        Optional<Student> existingStudent = studentDAO.getStudentById(id);
        if (existingStudent.isEmpty()) {
            logger.error("Errors: Update Student failed. Student with ID {} does not exist.", id);
            return false;
        }

        // Validate properties
        if (!ValidationService.isValidName(name)) {
            logger.error("Errors: Update Student failed due to invalid name: '{}'", name);
            return false;
        }
        if (!ValidationService.isValidAge(age)) {
            logger.error("Errors: Update Student failed due to invalid age: {}", age);
            return false;
        }
        if (!ValidationService.isValidDepartment(department)) {
            logger.error("Errors: Update Student failed due to invalid department: '{}'", department);
            return false;
        }
        if (!ValidationService.isValidRole(role)) {
            logger.error("Errors: Update Student failed due to invalid role: '{}'", role);
            return false;
        }
        if (!ValidationService.isValidAddress(address)) {
            logger.error("Errors: Update Student failed due to invalid address: '{}'", address);
            return false;
        }

        // Map updated parameters to entity
        Student updatedStudent = new Student(id, name, age, department, role, address);

        // Run DAO update
        boolean isSuccess = studentDAO.updateStudent(updatedStudent);
        if (isSuccess) {
            logger.info("Student records updated successfully for ID: {}", id);
        }
        return isSuccess;
    }

    /**
     * Deletes a student record.
     * 
     * @param id the student's ID
     * @return true if deletion succeeded, false otherwise
     */
    public boolean deleteStudent(int id) {
        // Ensure student exists
        Optional<Student> existingStudent = studentDAO.getStudentById(id);
        if (existingStudent.isEmpty()) {
            logger.error("Errors: Delete Student failed. Student with ID {} does not exist.", id);
            return false;
        }

        // Execute DAO delete query
        boolean isSuccess = studentDAO.deleteStudent(id);
        if (isSuccess) {
            logger.info("Student records deleted successfully for ID: {}", id);
        }
        return isSuccess;
    }

    /**
     * Pulls all student records from database and exports them into JSON file structure.
     * 
     * @return true if serialization and file writing succeeds, false otherwise
     */
    public boolean exportStudentsToJson() {
        List<Student> allStudents = studentDAO.getAllStudents();
        return jsonService.exportToJson(allStudents);
    }
}
