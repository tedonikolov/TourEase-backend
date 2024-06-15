package com.tourease.email.models.dto;

import java.math.BigDecimal;

public record PaymentChangeReservationVO(
        Long reservationNumber,
        String name,
        String email,
        BigDecimal newPrice,
        BigDecimal oldPrice,
        String currency
) {
}
