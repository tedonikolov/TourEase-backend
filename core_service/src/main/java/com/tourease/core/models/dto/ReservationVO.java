package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationVO(CustomerVO customer,
                            Long hotelId,
                            Long typeId,
                            Long mealId,
                            OffsetDateTime checkIn,
                            OffsetDateTime checkOut,
                            Integer nights,
                            BigDecimal price,
                            Integer peopleCount,
                            Currency currency) {
    public ReservationVO(ReservationCreateDTO reservationCreateDTO, UserVO customer) {
        this(new CustomerVO(customer),
                reservationCreateDTO.hotelId(),
                reservationCreateDTO.typeId(),
                reservationCreateDTO.mealId(),
                reservationCreateDTO.checkIn(),
                reservationCreateDTO.checkOut(),
                reservationCreateDTO.nights(),
                reservationCreateDTO.price(),
                reservationCreateDTO.peopleCount(),
                reservationCreateDTO.currency());
    }
}
