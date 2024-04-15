package com.tourease.configuration.models.entities;

import com.tourease.configuration.models.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "currency_rate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate {
    @Id
    @Column(name = "id", nullable = false)
    @Enumerated(EnumType.STRING)
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
