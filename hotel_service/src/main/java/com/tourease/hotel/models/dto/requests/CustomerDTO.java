package com.tourease.hotel.models.dto.requests;

import java.time.LocalDate;

public record CustomerDTO(Long id,
                          String fullName,
                          String phoneNumber,
                          String passportId,
                          LocalDate birthDate,
                          LocalDate creationDate,
                          LocalDate expirationDate,
                          String country,
                          String gender) {
}