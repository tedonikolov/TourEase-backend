package com.tourease.hotel.models.dto.requests;


import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationClientUpdateVO(
                                  Long reservationNumber,
                                  OffsetDateTime checkIn,
                                  OffsetDateTime checkOut,
                                  Integer nights,
                                  Integer peopleCount,
                                  BigDecimal price,
                                  Currency currency) {
}