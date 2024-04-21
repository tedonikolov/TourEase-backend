package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservationUpdateVO(Long id,
                                  Long roomId,
                                  Long typeId,
                                  Long mealId,
                                  List<Long> customers,
                                  OffsetDateTime checkIn,
                                  OffsetDateTime checkOut,
                                  Integer nights,
                                  Integer peopleCount,
                                  BigDecimal price,
                                  Currency currency) {
}