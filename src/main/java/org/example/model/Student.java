package org.example.model;

/**
 * Model class representing a Student in the system.
 * Updated to replace cgpa with role (e.g. Developer, Tester) and address (e.g. Chennai, Coimbatore).
 */
public class Student {
    
    // Unique identifier for the student (assigned by the database)
    private Integer id;
    
    // Full name of the student
    private String name;
    
    // Age of the student
    private int age;
    
    // Department of study
    private String department;
    
    // Job/Career role (e.g. Developer, Tester, etc.)
    private String role;
    
    // Address location (e.g. Chennai, Coimbatore, etc.)
    private String address;

    /**
     * Default constructor.
     */
    public Student() {
    }

    /**
     * Constructor without ID. Used when creating a new student before inserting into the database.
     * 
     * @param name       the student's name
     * @param age        the student's age
     * @param department the student's department
     * @param role       the student's role
     * @param address    the student's address location
     */
    public Student(String name, int age, String department, String role, String address) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.role = role;
        this.address = address;
    }

    /**
     * All-arguments constructor. Used when loading a student from the database.
     * 
     * @param id         the student's ID
     * @param name       the student's name
     * @param age        the student's age
     * @param department the student's department
     * @param role       the student's role
     * @param address    the student's address location
     */
    public Student(Integer id, String name, int age, String department, String role, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.role = role;
        this.address = address;
    }

    /**
     * Gets the student ID.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the student ID.
     * @param id the ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the student's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student's name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the student's age.
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the student's age.
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the student's department.
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the student's department.
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the student's role.
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the student's role.
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the student's address.
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the student's address.
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns a string representation of the Student object.
     * @return string representation
     */
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
