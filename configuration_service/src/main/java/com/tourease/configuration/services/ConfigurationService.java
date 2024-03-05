package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.response.AllConfigurations;
import com.tourease.configuration.models.dto.response.EmailInfoVO;
import com.tourease.configuration.models.entities.Configuration;
import com.tourease.configuration.models.enums.Field;
import com.tourease.configuration.repositories.ConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    public AllConfigurations getAllConfigurations() {
        return new AllConfigurations(getEmailInfo());
    }

    public EmailInfoVO getEmailInfo() {
        Configuration email = configurationRepository.findByName(Field.EMAIL_FROM);
        Configuration password = configurationRepository.findByName(Field.EMAIL_PASSWORD);

        return new EmailInfoVO(email.getValue(), password.getValue());
    }
}
