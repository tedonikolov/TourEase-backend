package com.tourease.hotel.models.enums;

import com.tourease.hotel.models.dto.response.CurrencyRateVO;

import java.math.BigDecimal;
import java.util.List;

public enum Currency {
    BGN, EUR, RUB, USD, GBP, RON, TRY;

    public static BigDecimal getRate(Currency currency, Currency newCurrency, List<CurrencyRateVO> currencyRates) {
        return switch (currency) {
            case BGN ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateBGN();
            case USD ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateUSD();
            case EUR ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateEUR();
            case GBP ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateGBP();
            case RUB ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateRUB();
            case RON ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateRON();
            case TRY ->
                    currencyRates.stream().filter(currencyRateVO -> currencyRateVO.currency().equals(newCurrency)).findFirst().orElseThrow().rateTRY();
        };
    }
}

