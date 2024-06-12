package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationCreateDTO(Long hotelId,
                                   Long typeId,
                                   Long mealId,
                                   Long roomId,
                                   OffsetDateTime checkIn,
                                   OffsetDateTime checkOut,
                                   Integer nights,
                                   BigDecimal price,
                                   Integer peopleCount,
                                   Currency currency) {
}