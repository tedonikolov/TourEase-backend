package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationCreateDTO(CustomerDTO customer,
                                   Long roomId,
                                   OffsetDateTime checkIn,
                                   OffsetDateTime checkOut,
                                   Integer nights,
                                   BigDecimal price,
                                   Currency currency) {
}