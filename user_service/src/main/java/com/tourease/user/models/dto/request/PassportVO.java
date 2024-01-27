package com.tourease.user.models.dto.request;

import com.tourease.user.validation.annotation.PassportDate;

import java.time.LocalDate;

@PassportDate
public record PassportVO(
        String email,
        String passportId,
        LocalDate creationDate,
        LocalDate expirationDate,
        String country) {
}
