package com.tourease.configuration.repositories;

import com.tourease.configuration.models.collections.CurrencyRate;
import com.tourease.configuration.models.enums.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends MongoRepository<CurrencyRate, Currency> {
}