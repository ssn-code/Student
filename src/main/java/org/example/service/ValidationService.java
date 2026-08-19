package org.example.service;

import org.apache.commons.lang3.StringUtils;

/**
 * Validation service responsible for validating input data before database persistence.
 * Leverages Apache Commons Lang3 for string validations.
 */
public class ValidationService {

    /**
     * Validates the student's name.
     * Name must not be blank, empty, and must not exceed 100 characters.
     * 
     * @param name the student's name to validate
     * @return true if name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return StringUtils.isNotBlank(name) && name.trim().length() <= 100;
    }

    /**
     * Validates the student's age.
     * Age must be in a standard, realistic student range (e.g. 15 to 100 years old).
     * 
     * @param age the student's age to validate
     * @return true if age is valid, false otherwise
     */
    public static boolean isValidAge(int age) {
        return age >= 15 && age <= 100;
    }

    /**
     * Validates the student's department.
     * Department must not be blank, empty, and must not exceed 100 characters.
     * 
     * @param department the department to validate
     * @return true if department is valid, false otherwise
     */
    public static boolean isValidDepartment(String department) {
        return StringUtils.isNotBlank(department) && department.trim().length() <= 100;
    }

    /**
     * Validates the student's role.
     * Role must not be blank, empty, and must not exceed 100 characters.
     * 
     * @param role the role to validate
     * @return true if role is valid, false otherwise
     */
    public static boolean isValidRole(String role) {
        return StringUtils.isNotBlank(role) && role.trim().length() <= 100;
    }

    /**
     * Validates the student's address location.
     * Address must not be blank, empty, and must not exceed 100 characters.
     * 
     * @param address the address to validate
     * @return true if address is valid, false otherwise
     */
    public static boolean isValidAddress(String address) {
        return StringUtils.isNotBlank(address) && address.trim().length() <= 100;
    }
}
