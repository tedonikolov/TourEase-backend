package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.response.Countries;
import com.tourease.configuration.models.entities.Country;
import com.tourease.configuration.repositories.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConfigurationService {
    private final CountryRepository countryRepository;

    public Countries getAllCountries() {
        List<Country> countries = countryRepository.findAll();

        return new Countries(countries.stream().map(Country::getCountry).toList());
    }
}
