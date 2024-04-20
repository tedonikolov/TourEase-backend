package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.MealType;

import java.math.BigDecimal;

public record MealVo(Long id,
                     Long hotelId,
                     MealType type,
                     BigDecimal price,
                     Currency currency) {
}
