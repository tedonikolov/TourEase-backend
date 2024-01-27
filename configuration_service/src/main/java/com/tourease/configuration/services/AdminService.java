package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.request.UpdateConfigurationDTO;
import com.tourease.configuration.models.entities.Configuration;
import com.tourease.configuration.models.enums.Field;
import com.tourease.configuration.repositories.ConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final ConfigurationRepository configurationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void save(List<UpdateConfigurationDTO> configurationsVO) {
        for(UpdateConfigurationDTO updateConfiguration:configurationsVO){
            Configuration configuration = configurationRepository.findByName(Field.valueOfLabel(updateConfiguration.getName()));
            configuration.setValue(updateConfiguration.getValue());
            configurationRepository.save(configuration);
            kafkaTemplate.send("configuration_service","admin","Configurations uppdated!");
        }
    }
}
