package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;

public record BedVO(
        Long id,
        String name,
        int people,
        BigDecimal price,
        Currency currency
) {
}
