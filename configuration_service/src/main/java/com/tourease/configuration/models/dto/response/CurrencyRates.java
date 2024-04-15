package com.tourease.configuration.models.dto.response;

import java.math.BigDecimal;

public record CurrencyRates(
        BigDecimal EUR,
        BigDecimal USD,
        BigDecimal GBP,
        BigDecimal RUB,
        BigDecimal BGN,
        BigDecimal RON,
        BigDecimal TRY
) {
}
