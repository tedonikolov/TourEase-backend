package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationCreateDTO(Long hotelId,
                                   Long typeId,
                                   Long mealId,
                                   Long roomId,
                                   LocalDate checkIn,
                                   LocalDate checkOut,
                                   Integer nights,
                                   BigDecimal price,
                                   BigDecimal mealPrice,
                                   BigDecimal nightPrice,
                                   Integer peopleCount,
                                   Currency currency) {
}