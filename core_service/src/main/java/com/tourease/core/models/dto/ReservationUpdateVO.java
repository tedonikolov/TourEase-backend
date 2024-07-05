package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationUpdateVO(Long reservationNumber,
                                  LocalDate checkIn,
                                  LocalDate checkOut,
                                  Integer nights,
                                  Integer peopleCount,
                                  BigDecimal price,
                                  Currency currency,
                                  String name,
                                  String email
) {
}