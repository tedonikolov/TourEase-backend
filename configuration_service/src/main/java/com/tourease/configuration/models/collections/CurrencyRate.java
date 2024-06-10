package com.tourease.configuration.models.collections;

import com.tourease.configuration.models.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "currency_rate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate {
    @Id
    private Currency currency;
    private BigDecimal rateBGN;
    private BigDecimal rateEUR;
    private BigDecimal rateRUB;
    private BigDecimal rateUSD;
    private BigDecimal rateGBP;
    private BigDecimal rateRON;
    private BigDecimal rateTRY;

    public CurrencyRate(Currency currency){
        this.currency=currency;
    }
}
