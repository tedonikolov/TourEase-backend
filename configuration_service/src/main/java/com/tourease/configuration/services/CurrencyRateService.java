package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.response.CurrencyRates;
import com.tourease.configuration.models.collections.CurrencyRate;
import com.tourease.configuration.models.enums.Currency;
import com.tourease.configuration.repositories.CurrencyRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyRateService {
    private final CurrencyRateRepository currencyRateRepository;

    public void setRates(Currency currency, CurrencyRates currencyRates) {
        currencyRateRepository.findById(currency).ifPresent(
                currencyRate -> {
                    currencyRate.setRateBGN(currencyRates.BGN());
                    currencyRate.setRateEUR(currencyRates.EUR());
                    currencyRate.setRateUSD(currencyRates.USD());
                    currencyRate.setRateGBP(currencyRates.GBP());
                    currencyRate.setRateRUB(currencyRates.RUB());
                    currencyRate.setRateRON(currencyRates.RON());
                    currencyRate.setRateTRY(currencyRates.TRY());
                    currencyRateRepository.save(currencyRate);
                });

    }

    public List<CurrencyRate> getRates() {
        return currencyRateRepository.findAll();
    }
}
