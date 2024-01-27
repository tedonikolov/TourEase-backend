package com.tourease.user.validation;


import com.tourease.user.services.UserService;
import com.tourease.user.validation.annotation.ExistingEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;



@Component
public class ExistingEmailValidation implements ConstraintValidator<ExistingEmail, String> {
    private final UserService userService;

    public ExistingEmailValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(ExistingEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.isEmailTaken(email);
    }
}
