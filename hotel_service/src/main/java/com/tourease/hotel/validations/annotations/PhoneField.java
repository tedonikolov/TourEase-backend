package com.tourease.hotel.validations.annotations;

import com.tourease.hotel.validations.PhoneFieldNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneFieldNumberValidator.class)
public @interface PhoneField {

    String message() default "Phone number invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
