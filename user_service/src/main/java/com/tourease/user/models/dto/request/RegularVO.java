package com.tourease.user.models.dto.request;

import com.tourease.user.validation.annotation.Phone;

import java.time.LocalDate;

@Phone
public record RegularVO(
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDate birthDate,
        String country,
        String gender
) {
}
