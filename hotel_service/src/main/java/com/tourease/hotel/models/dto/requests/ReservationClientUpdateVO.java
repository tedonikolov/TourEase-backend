package com.tourease.hotel.models.dto.requests;


import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationClientUpdateVO(
                                  Long reservationNumber,
                                  LocalDate checkIn,
                                  LocalDate checkOut,
                                  Integer nights,
                                  Integer peopleCount,
                                  BigDecimal price,
                                  Currency currency,
                                  String name,
                                  String email) {
}