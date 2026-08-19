package org.example.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationServiceTest {

    @Test
    public void testIsValidName() {
        assertTrue(ValidationService.isValidName("John Doe"));
        assertFalse(ValidationService.isValidName(""));
        assertFalse(ValidationService.isValidName(null));
        assertFalse(ValidationService.isValidName("   "));
        assertFalse(ValidationService.isValidName("a".repeat(101)));
    }

    @Test
    public void testIsValidAge() {
        assertTrue(ValidationService.isValidAge(15));
        assertTrue(ValidationService.isValidAge(25));
        assertTrue(ValidationService.isValidAge(100));
        assertFalse(ValidationService.isValidAge(14));
        assertFalse(ValidationService.isValidAge(101));
    }

    @Test
    public void testIsValidDepartment() {
        assertTrue(ValidationService.isValidDepartment("Computer Science"));
        assertFalse(ValidationService.isValidDepartment(""));
        assertFalse(ValidationService.isValidDepartment(null));
    }

    @Test
    public void testIsValidRole() {
        assertTrue(ValidationService.isValidRole("Developer"));
        assertFalse(ValidationService.isValidRole(""));
        assertFalse(ValidationService.isValidRole(null));
    }

    @Test
    public void testIsValidAddress() {
        assertTrue(ValidationService.isValidAddress("Chennai"));
        assertFalse(ValidationService.isValidAddress(""));
        assertFalse(ValidationService.isValidAddress(null));
    }
}
