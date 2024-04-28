package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;
import com.tourease.core.models.enums.MealType;

import java.math.BigDecimal;

public record MealVO(
        Long id,
        MealType type,
        BigDecimal price,
        Currency currency
) {
}
