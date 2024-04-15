package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.response.ApiResponse;
import com.tourease.configuration.models.dto.response.CurrencyRates;
import com.tourease.configuration.models.enums.Currency;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ExchangeRatesClient {
    @Qualifier("rapidApiRestTemplate")
    private final RestTemplate rapidApiRestTemplate;

    public CurrencyRates getExchangeRates(Currency currency){
        return rapidApiRestTemplate.getForObject("/latest?base="+currency.name(), ApiResponse.class).rates();
    }

}
