package org.example.util;

import org.example.model.Student;
import org.example.service.StudentService;
import org.example.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menu class managing the command-line interface (CLI) for the Student Management System.
 * Implements input validation wrappers and prints interactive menus and tabular data views.
 */
public class Menu {

    // Logger instance for Menu interactions and validation errors
    private static final Logger logger = LoggerFactory.getLogger(Menu.class);

    // Orchestrator service layer
    private final StudentService studentService;

    /**
     * Constructor initializing the Menu with the StudentService reference.
     */
    public Menu() {
        this.studentService = new StudentService();
    }

    /**
     * Entry point to run the menu event loop.
     * Continues presenting options and handling requests until user chooses to exit.
     */
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("  STUDENT MANAGEMENT SYSTEM   ");
            System.out.println("==============================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Export to JSON");
            System.out.println("7. Exit");
            System.out.println("==============================");
            
            int choice = readIntInput(scanner, "Select an option (1-7): ");

            switch (choice) {
                case 1:
                    handleAddStudent(scanner);
                    break;
                case 2:
                    handleViewStudents();
                    break;
                case 3:
                    handleSearchStudent(scanner);
                    break;
                case 4:
                    handleUpdateStudent(scanner);
                    break;
                case 5:
                    handleDeleteStudent(scanner);
                    break;
                case 6:
                    handleExportToJson();
                    break;
                case 7:
                    System.out.println("Thank you for using Student Management System. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please enter a number between 1 and 7.");
                    break;
            }
        }
        scanner.close();
    }

    /**
     * Interactively asks user details and registers a new student.
     * 
     * @param scanner CLI Scanner
     */
    private void handleAddStudent(Scanner scanner) {
        System.out.println("\n--- Add Student ---");
        
        String name = readStringInput(scanner, "Enter Name: ");
        if (!ValidationService.isValidName(name)) {
            System.out.println("Error: Name cannot be blank or exceed 100 characters.");
            return;
        }

        int age = readIntInput(scanner, "Enter Age: ");
        if (!ValidationService.isValidAge(age)) {
            System.out.println("Error: Age must be a positive integer (typically between 15 and 100).");
            return;
        }

        String department = readStringInput(scanner, "Enter Department: ");
        if (!ValidationService.isValidDepartment(department)) {
            System.out.println("Error: Department cannot be blank or exceed 100 characters.");
            return;
        }

        String role = readStringInput(scanner, "Enter Role (e.g. Developer, Tester): ");
        if (!ValidationService.isValidRole(role)) {
            System.out.println("Error: Role cannot be blank or exceed 100 characters.");
            return;
        }

        String address = readStringInput(scanner, "Enter Address Location (e.g. Chennai, Coimbatore): ");
        if (!ValidationService.isValidAddress(address)) {
            System.out.println("Error: Address cannot be blank or exceed 100 characters.");
            return;
        }

        boolean success = studentService.addStudent(name, age, department, role, address);
        if (success) {
            System.out.println("Success: Student added successfully!");
        } else {
            System.out.println("Error: Failed to add student. Check application logs for details.");
        }
    }

    /**
     * Displays all student records in a structured, clean ASCII table format.
     */
    private void handleViewStudents() {
        System.out.println("\n--- View Students ---");
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No student records found in the database.");
            return;
        }

        // Print table header borders
        System.out.println("+-------+--------------------------------+-----+----------------------+----------------------+----------------------+");
        System.out.printf("| %-5s | %-30s | %-3s | %-20s | %-20s | %-20s |%n", "ID", "Name", "Age", "Department", "Role", "Address");
        System.out.println("+-------+--------------------------------+-----+----------------------+----------------------+----------------------+");

        // Print rows
        for (Student s : students) {
            System.out.printf("| %-5d | %-30s | %-3d | %-20s | %-20s | %-20s |%n",
                    s.getId(), s.getName(), s.getAge(), s.getDepartment(), s.getRole(), s.getAddress());
        }

        // Print table footer border
        System.out.println("+-------+--------------------------------+-----+----------------------+----------------------+----------------------+");
    }

    /**
     * Interactively prompts for an ID and prints the corresponding student's details.
     * 
     * @param scanner CLI Scanner
     */
    private void handleSearchStudent(Scanner scanner) {
        System.out.println("\n--- Search Student ---");
        int id = readIntInput(scanner, "Enter Student ID to Search: ");

        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isPresent()) {
            Student s = studentOpt.get();
            System.out.println("Student Record Found:");
            System.out.println("---------------------");
            System.out.printf("ID:         %d%n", s.getId());
            System.out.printf("Name:       %s%n", s.getName());
            System.out.printf("Age:        %d%n", s.getAge());
            System.out.printf("Department: %s%n", s.getDepartment());
            System.out.printf("Role:       %s%n", s.getRole());
            System.out.printf("Address:    %s%n", s.getAddress());
            System.out.println("---------------------");
        } else {
            System.out.printf("Record not found for Student ID: %d.%n", id);
        }
    }

    /**
     * Interactively prompts for fields and updates an existing student record.
     * 
     * @param scanner CLI Scanner
     */
    private void handleUpdateStudent(Scanner scanner) {
        System.out.println("\n--- Update Student ---");
        int id = readIntInput(scanner, "Enter Student ID to Update: ");

        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isEmpty()) {
            System.out.printf("Error: Student ID %d does not exist.%n", id);
            return;
        }

        Student s = studentOpt.get();
        System.out.printf("Current Record: Name='%s', Age=%d, Dept='%s', Role='%s', Address='%s'%n",
                s.getName(), s.getAge(), s.getDepartment(), s.getRole(), s.getAddress());
        System.out.println("Please enter the updated details:");

        String name = readStringInput(scanner, "Enter New Name: ");
        if (!ValidationService.isValidName(name)) {
            System.out.println("Error: Name cannot be blank or exceed 100 characters.");
            return;
        }

        int age = readIntInput(scanner, "Enter New Age: ");
        if (!ValidationService.isValidAge(age)) {
            System.out.println("Error: Age must be a positive integer (typically between 15 and 100).");
            return;
        }

        String department = readStringInput(scanner, "Enter New Department: ");
        if (!ValidationService.isValidDepartment(department)) {
            System.out.println("Error: Department cannot be blank or exceed 100 characters.");
            return;
        }

        String role = readStringInput(scanner, "Enter New Role: ");
        if (!ValidationService.isValidRole(role)) {
            System.out.println("Error: Role cannot be blank or exceed 100 characters.");
            return;
        }

        String address = readStringInput(scanner, "Enter New Address: ");
        if (!ValidationService.isValidAddress(address)) {
            System.out.println("Error: Address cannot be blank or exceed 100 characters.");
            return;
        }

        boolean success = studentService.updateStudent(id, name, age, department, role, address);
        if (success) {
            System.out.println("Success: Student updated successfully!");
        } else {
            System.out.println("Error: Failed to update student. Check application logs for details.");
        }
    }

    /**
     * Interactively prompts for ID and deletes the matching record from database.
     * 
     * @param scanner CLI Scanner
     */
    private void handleDeleteStudent(Scanner scanner) {
        System.out.println("\n--- Delete Student ---");
        int id = readIntInput(scanner, "Enter Student ID to Delete: ");

        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isEmpty()) {
            System.out.printf("Error: Student ID %d does not exist.%n", id);
            return;
        }

        boolean success = studentService.deleteStudent(id);
        if (success) {
            System.out.println("Success: Student record deleted successfully!");
        } else {
            System.out.println("Error: Failed to delete student. Check application logs for details.");
        }
    }

    /**
     * Triggers JSON serialization and file writing.
     */
    private void handleExportToJson() {
        System.out.println("\n--- Export to JSON ---");
        boolean success = studentService.exportStudentsToJson();
        if (success) {
            System.out.println("Success: Student records exported to 'students.json' successfully!");
        } else {
            System.out.println("Error: Export failed. Check application logs for details.");
        }
    }

    /**
     * Safely reads string input from user, preventing blank strings.
     */
    private String readStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Safely reads integer input, handling invalid format exceptions without crashing.
     */
    private int readIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid integer.");
            }
        }
    }
}
