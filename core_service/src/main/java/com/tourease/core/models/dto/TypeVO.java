package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public record TypeVO(
        Long id,
        String name,
        BigDecimal price,
        Currency currency,
        List<BedVO> beds
) {
}
