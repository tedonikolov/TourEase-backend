package com.tourease.user.models.dto.request;

import java.time.LocalDate;

public record RegularVO(
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String country,
        String gender
) {
}
