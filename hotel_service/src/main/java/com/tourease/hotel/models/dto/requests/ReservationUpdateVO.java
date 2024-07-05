package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReservationUpdateVO(Long id,
                                  Long roomId,
                                  Long typeId,
                                  Long mealId,
                                  List<Long> customers,
                                  LocalDate checkIn,
                                  LocalDate checkOut,
                                  Integer nights,
                                  Integer peopleCount,
                                  BigDecimal price,
                                  BigDecimal mealPrice,
                                  BigDecimal nightPrice,
                                  BigDecimal discount,
                                  BigDecimal advancedPayment,
                                  Currency currency) {
}