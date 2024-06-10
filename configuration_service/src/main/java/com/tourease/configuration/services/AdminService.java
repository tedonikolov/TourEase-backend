package com.tourease.configuration.services;

import com.tourease.configuration.models.dto.request.UpdateConfigurationDTO;
import com.tourease.configuration.models.dto.response.AllConfigurations;
import com.tourease.configuration.models.collections.Configuration;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final ConfigurationService configurationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void save(List<UpdateConfigurationDTO> configurationsVO) {
        for(UpdateConfigurationDTO updateConfiguration:configurationsVO){
            Configuration configuration = configurationService.findByName(updateConfiguration.getName());
            configuration.setValue(updateConfiguration.getValue());
            configurationService.save(configuration);
            kafkaTemplate.send("configuration_service","admin","Configurations updated!");
        }
    }

    public AllConfigurations getAllConfigurations() {
        return new AllConfigurations(configurationService.getEmailInfo());
    }

}
