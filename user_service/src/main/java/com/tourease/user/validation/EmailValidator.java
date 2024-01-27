package com.tourease.user.validation;


import com.tourease.user.validation.annotation.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {
    private final String regex="^[\\w-\\.]{1,255}@([\\w-]{1,255}\\.)+[\\w-]{2,4}$";

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email.matches(regex);
    }
}
