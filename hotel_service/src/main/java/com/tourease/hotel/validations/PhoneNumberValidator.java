package com.tourease.hotel.validations;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import com.tourease.hotel.models.dto.requests.OwnerSaveVO;
import com.tourease.hotel.validations.annotations.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<Phone, OwnerSaveVO> {

    @Override
    public void initialize(Phone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(OwnerSaveVO owner, ConstraintValidatorContext context) {
        try {
            String countryCode = CountryCode.findByName(owner.country()).get(0).name();
            String phoneNumber = owner.phone();

            if (phoneNumber == null || phoneNumber.isBlank()) return false;

            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

            Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());

            return phoneNumberUtil.isValidNumberForRegion(phone, countryCode);
        } catch (NumberParseException | IndexOutOfBoundsException e) {
            return false;
        }
    }

}
