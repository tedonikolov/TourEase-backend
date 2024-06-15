package com.tourease.email.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationVO(
        Long reservationNumber,
        LocalDate checkIn,
        LocalDate checkOut,
        Integer nights,
        Integer people,
        String roomType,
        String meal,
        BigDecimal price,
        String currency,
        String hotelName,
        String hotelCountry,
        String hotelCity,
        String hotelAddress,
        String workerName,
        String workerEmail,
        String workerPhone
        ) {
}
