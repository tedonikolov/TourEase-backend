package com.tourease.configuration.services;

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

    public EmailInfoVO getEmailInfo() {
        Configuration email = configurationRepository.findByName(Field.EMAIL_FROM);
        Configuration password = configurationRepository.findByName(Field.EMAIL_PASSWORD);
        Configuration activateProfileURL = configurationRepository.findByName(Field.ACTIVATE_PROFILE_URL);
        Configuration passportExpiredURL = configurationRepository.findByName(Field.PASSPORT_EXPIRED_URL);
        Configuration changePasswordURL = configurationRepository.findByName(Field.CHANGE_PASSWORD_URL);

        return new EmailInfoVO(email.getValue(), password.getValue(), activateProfileURL.getValue(), passportExpiredURL.getValue(), changePasswordURL.getValue());
    }

    public Configuration findByName(String name){
        return configurationRepository.findByName(Field.valueOfLabel(name));
    }

    public void save(Configuration configuration){
        configurationRepository.save(configuration);
    }
}
