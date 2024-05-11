package com.tourease.core.models.dto;

import java.time.LocalDate;

public record PassportVO(
        String passportId,
        LocalDate creationDate,
        LocalDate expirationDate,
        Boolean expired,
        String country
) {
}
