package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationVO(CustomerVO customer,
                            Long hotelId,
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
    public ReservationVO(ReservationCreateDTO reservationCreateDTO, UserVO customer) {
        this(new CustomerVO(customer),
                reservationCreateDTO.hotelId(),
                reservationCreateDTO.typeId(),
                reservationCreateDTO.mealId(),
                reservationCreateDTO.roomId(),
                reservationCreateDTO.checkIn(),
                reservationCreateDTO.checkOut(),
                reservationCreateDTO.nights(),
                reservationCreateDTO.price(),
                reservationCreateDTO.mealPrice(),
                reservationCreateDTO.nightPrice(),
                reservationCreateDTO.peopleCount(),
                reservationCreateDTO.currency());
    }
}
