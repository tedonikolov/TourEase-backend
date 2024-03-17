package com.tourease.hotel.validations;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tourease.hotel.validations.annotations.PhoneField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneFieldNumberValidator implements ConstraintValidator<PhoneField, String> {

    @Override
    public void initialize(PhoneField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        try {
            if (phone == null || phone.isBlank()) return false;

            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());

            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException | IndexOutOfBoundsException e) {
            return false;
        }
    }

}
