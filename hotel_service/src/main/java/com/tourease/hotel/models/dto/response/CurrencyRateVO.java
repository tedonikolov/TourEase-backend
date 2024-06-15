package com.tourease.hotel.models.dto.response;


import com.tourease.hotel.models.enums.Currency;

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
