package com.tourease.user.validation.annotation;

import com.tourease.user.validation.PassportDateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PassportDateValidation.class)
public @interface PassportDate {
    String message() default "Not valid passport date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
