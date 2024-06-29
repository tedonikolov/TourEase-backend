package com.tourease.hotel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


import com.tourease.hotel.models.dto.requests.OwnerSaveVO;
import com.tourease.hotel.validations.PhoneNumberValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhoneNumberValidatorTests {

    private PhoneNumberValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new PhoneNumberValidator();
    }

    @Test
    public void testValidPhoneNumber() {
        OwnerSaveVO owner = new OwnerSaveVO(1L, "Ivan Petrov", "Company",
                "Address", "+359884987884", "Bulgaria", "Varna", "EIK");
        boolean isValid = validator.isValid(owner, mock(ConstraintValidatorContext.class));

        assertTrue(isValid);
    }

    @Test
    public void testInvalidPhoneNumber() {
        OwnerSaveVO owner = new OwnerSaveVO(1L, "Ivan Petrov", "Company",
                "Address", "0887898", "Bulgaria", "City", "EIK");
        boolean isValid = validator.isValid(owner, mock(ConstraintValidatorContext.class));

        assertFalse(isValid);

        owner = new OwnerSaveVO(1L, "Ivan Petrov", "Company",
                "Address", "+359884987884", "Germany", "Berlin", "EIK");
        isValid = validator.isValid(owner, mock(ConstraintValidatorContext.class));

        assertFalse(isValid);
    }

    @Test
    public void testNullPhoneNumber() {
        OwnerSaveVO owner = new OwnerSaveVO(1L, "Ivan Petrov", "Company",
                "Address", null, "States", "City", "EIK");
        boolean isValid = validator.isValid(owner, mock(ConstraintValidatorContext.class));

        assertFalse(isValid);
    }
}
