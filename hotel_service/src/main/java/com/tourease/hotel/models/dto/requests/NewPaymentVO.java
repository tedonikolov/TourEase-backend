package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.PaymentType;

import java.math.BigDecimal;

public record NewPaymentVO(
        Long customerId,
        Long hotelId,
        BigDecimal price,
        Currency currency,
        PaidFor paidFor,
        PaymentType paymentType
) {
}
