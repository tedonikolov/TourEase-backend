package com.tourease.configuration;

import com.tourease.configuration.models.entities.Configuration;
import com.tourease.configuration.models.enums.Field;
import com.tourease.configuration.repositories.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialDataSetup implements CommandLineRunner {

    private final ConfigurationRepository configurationRepository;

    @Override
    public void run(String... args) {
        initData();
    }

    private void initData() {
        if (configurationRepository.count() == 0) {
            Configuration emailFrom = new Configuration(Field.EMAIL_FROM, "tekanpicha@gmail.com");
            Configuration emailPassword = new Configuration(Field.EMAIL_PASSWORD, "wlmkshrmalmltbxc");
            Configuration activateProfileURL = new Configuration(Field.ACTIVATE_PROFILE_URL, "http://localhost:3000/activateProfile?email=");
            Configuration passportExpiredURL = new Configuration(Field.PASSPORT_EXPIRED_URL, "http://localhost:3000/regular/profile?passportExpired=1");
            Configuration changePasswordURL = new Configuration(Field.CHANGE_PASSWORD_URL, "http://localhost:3000/changePassword?email=");

            configurationRepository.saveAll(List.of(emailFrom, emailPassword, activateProfileURL, passportExpiredURL, changePasswordURL));
        }
    }
}
