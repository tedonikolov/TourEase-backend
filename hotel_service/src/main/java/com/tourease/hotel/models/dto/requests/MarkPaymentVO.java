package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.PaymentType;

import java.math.BigDecimal;

public record MarkPaymentVO(
        Long id,
        PaymentType paymentType,
        Currency currency,
        BigDecimal price
) {
}
