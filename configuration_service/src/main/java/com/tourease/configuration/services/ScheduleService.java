package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.response.CurrencyRates;
import com.tourease.configuration.models.enums.Currency;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@EnableScheduling
public class ScheduleService {
    private final CurrencyRateService currencyRateService;
    private final ExchangeRatesClient exchangeRatesClient;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateRates() {
        CurrencyRates bgnRates = exchangeRatesClient.getExchangeRates(Currency.BGN);
        currencyRateService.setRates(Currency.BGN, bgnRates);

        CurrencyRates eurRates = exchangeRatesClient.getExchangeRates(Currency.EUR);
        currencyRateService.setRates(Currency.EUR, eurRates);

        CurrencyRates usdRates = exchangeRatesClient.getExchangeRates(Currency.USD);
        currencyRateService.setRates(Currency.USD, usdRates);

        CurrencyRates gbpRates = exchangeRatesClient.getExchangeRates(Currency.GBP);
        currencyRateService.setRates(Currency.GBP, gbpRates);

        CurrencyRates rubRates = exchangeRatesClient.getExchangeRates(Currency.RUB);
        currencyRateService.setRates(Currency.RUB, rubRates);

        CurrencyRates ronRates = exchangeRatesClient.getExchangeRates(Currency.RON);
        currencyRateService.setRates(Currency.RON, ronRates);

        CurrencyRates tryRates = exchangeRatesClient.getExchangeRates(Currency.TRY);
        currencyRateService.setRates(Currency.TRY, tryRates);
    }
}
