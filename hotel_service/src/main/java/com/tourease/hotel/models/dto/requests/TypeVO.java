package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public record TypeVO(Long id, String name, BigDecimal price, Currency currency, List<Long> beds, Long hotelId) {
}
