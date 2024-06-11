package com.tourease.core.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.core.models.dto.CurrencyRateVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class ConfigServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String configAppName = "CONFIGURATION-SERVICE";
    private final String configServiceUrl = "http://configuration-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(configAppName);
        if (service == null)
            throw new InternalServiceException("No connection to config-service.");
    }

    public List<CurrencyRateVO> getCurrencyRates() {
        return List.of(defaultRestTemplate.getForEntity(configServiceUrl + "/currency/getCurrencyRates", CurrencyRateVO[].class).getBody());
    }

}
