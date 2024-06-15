package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationUpdateVO(Long reservationNumber,
                                  OffsetDateTime checkIn,
                                  OffsetDateTime checkOut,
                                  Integer nights,
                                  Integer peopleCount,
                                  BigDecimal price,
                                  Currency currency,
                                  String name,
                                  String email
) {
}