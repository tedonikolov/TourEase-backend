package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.response.AllConfigurations;
import com.tourease.configuration.models.dto.response.EmailInfoVO;
import com.tourease.configuration.models.entities.Configuration;
import com.tourease.configuration.models.entities.Country;
import com.tourease.configuration.models.enums.Field;
import com.tourease.configuration.repositories.ConfigurationRepository;
import com.tourease.configuration.repositories.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConfigurationService {
    private final CountryRepository countryRepository;
    private final ConfigurationRepository configurationRepository;

    public List<String> getAllCountries() {
        List<Country> countries = countryRepository.findAll();

        return countries.stream().map(Country::getCountry).toList();
    }

    public AllConfigurations getAllConfigurations() {
        return new AllConfigurations(getEmailInfo());
    }

    public EmailInfoVO getEmailInfo() {
        Configuration email = configurationRepository.findByName(Field.EMAIL_FROM);
        Configuration password = configurationRepository.findByName(Field.EMAIL_PASSWORD);

        return new EmailInfoVO(email.getValue(), password.getValue());
    }
}
