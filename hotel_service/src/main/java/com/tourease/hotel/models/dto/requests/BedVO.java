package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;

public record BedVO(Long id, String name, int people, BigDecimal price, Long hotelId, Currency currency) {
}
