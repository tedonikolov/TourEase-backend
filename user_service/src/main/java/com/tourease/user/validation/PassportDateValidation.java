package com.tourease.user.validation;

import com.tourease.user.models.dto.request.PassportVO;
import com.tourease.user.validation.annotation.PassportDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PassportDateValidation implements ConstraintValidator<PassportDate, PassportVO> {
    @Override
    public void initialize(PassportDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PassportVO value, ConstraintValidatorContext context) {
        return value.creationDate().isBefore(value.expirationDate()) && value.expirationDate().isAfter(LocalDate.now());
    }
}
