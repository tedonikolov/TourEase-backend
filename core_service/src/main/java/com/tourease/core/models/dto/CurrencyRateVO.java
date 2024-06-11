package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;

import java.math.BigDecimal;

public record CurrencyRateVO(
        Currency currency,
        BigDecimal rateBGN,
        BigDecimal rateEUR,
        BigDecimal rateRUB,
        BigDecimal rateUSD,
        BigDecimal rateGBP,
        BigDecimal rateRON,
        BigDecimal rateTRY) {
}
