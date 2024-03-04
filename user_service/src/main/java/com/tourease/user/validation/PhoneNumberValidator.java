package com.tourease.user.validation;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import com.tourease.user.models.dto.request.RegularVO;
import com.tourease.user.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<Phone, RegularVO> {

    @Override
    public void initialize(Phone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegularVO regular, ConstraintValidatorContext context) {
        String countryCode = CountryCode.findByName(regular.country()).get(0).name();
        String phoneNumber = regular.phone();

        if (phoneNumber == null || phoneNumber.isBlank()) return true;

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phone = null;
        try {
            phone = phoneNumberUtil.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        } catch (NumberParseException e) {
            return false;
        }

        return phoneNumberUtil.isValidNumberForRegion(phone, countryCode);
    }

}
