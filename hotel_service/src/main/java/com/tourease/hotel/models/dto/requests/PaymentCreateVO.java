package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.PaidFor;

import java.math.BigDecimal;

public record PaymentCreateVO(
        Long customerId,
        Long hotelId,
        BigDecimal price,
        Currency currency,
        PaidFor paidFor,
        Long reservationNumber
) {
    public PaymentCreateVO (NewPaymentVO newPaymentVO){
        this(newPaymentVO.customerId(), newPaymentVO.hotelId(), newPaymentVO.price(), newPaymentVO.currency(), newPaymentVO.paidFor(), newPaymentVO.reservationNumber());
    }
}
