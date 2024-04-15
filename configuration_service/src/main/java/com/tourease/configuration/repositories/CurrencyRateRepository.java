package com.tourease.configuration.repositories;

import com.tourease.configuration.models.entities.CurrencyRate;
import com.tourease.configuration.models.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Currency> {
}