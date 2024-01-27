package com.tourease.user.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.user.models.dto.request.EmailInfoVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ConfigurationServiceClient {
    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    private final String configAppName = "CONFIGURATION-SERVICE";
    private final String configServiceUrl = "http://configuration-service";


    public void checkConnectionCore() {
        Application coreService = eurekaClient.getApplication(configAppName);
        if (coreService == null)
            throw new InternalServiceException("No connection to config-service.");
    }

    public EmailInfoVO getEmailInfo() {
        String url = configServiceUrl + "/internal/getEmailInfo";
        EmailInfoVO emailInfo = restTemplate.getForObject(url, EmailInfoVO.class);
        return emailInfo;
    }
}
