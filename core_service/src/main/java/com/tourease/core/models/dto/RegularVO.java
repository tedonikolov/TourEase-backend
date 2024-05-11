package com.tourease.core.models.dto;

import java.time.LocalDate;

public record RegularVO(
        String firstName,
        String lastName,
        LocalDate birthDate,

        String country,
        String gender,
        String phone,
        PassportVO passport
) {
}
