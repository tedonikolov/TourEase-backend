package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;

public record PaymentChangeReservationVO(
        Long reservationNumber,
        String name,
        String email,
        BigDecimal newPrice,
        BigDecimal oldPrice,
        Currency currency
) {
}
