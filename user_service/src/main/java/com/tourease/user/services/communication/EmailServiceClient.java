package com.tourease.user.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EmailServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String emailAppName = "EMAIL-SERVICE";
    private final String userServiceUrl = "http://email-service";

    public void checkConnection(String error) {
        Application service = eurekaClient.getApplication(emailAppName);
        if (service == null)
            throw new InternalServiceException(error);
    }

    public void sendActivationMail(String email) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/sendActivationMail?email="+email,null);
    }

    public void sendPasswordChangeLink(String email) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/sendPasswordChangeLink?email="+email, null);
    }

    public void sendPassportDateExpiredNotify(String email, String fullName) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/sendPassportDateExpiredNotify?email="+email+"&fullname="+fullName, null);
    }
}
