package com.tourease.user.validation.annotation;


import com.tourease.user.validation.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface Phone {

    String message() default "Phone number invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
