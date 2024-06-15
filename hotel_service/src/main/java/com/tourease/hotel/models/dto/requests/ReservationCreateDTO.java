package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationCreateDTO(CustomerDTO customer,
                                   Long hotelId,
                                   Long roomId,
                                   Long typeId,
                                   Long mealId,
                                   OffsetDateTime checkIn,
                                   OffsetDateTime checkOut,
                                   Integer nights,
                                   BigDecimal price,
                                   BigDecimal mealPrice,
                                   BigDecimal nightPrice,
                                   BigDecimal discount,
                                   BigDecimal advancedPayment,
                                   Integer peopleCount,
                                   Currency currency) {
}