package com.tourease.hotel.models.dto.requests;

import java.time.LocalDate;

public record CustomerDTO(String fullName,
                          String phone,
                          String passportId,
                          LocalDate birthDate,
                          LocalDate creationDate,
                          LocalDate expirationDate,
                          String country,
                          String gender) {
}