package com.tourease.user.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthenticationServiceClient {
    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    private final String authAppName = "AUTHENTICATION-SERVICE";
    private final String authServiceUrl = "http://authentication-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(authAppName);
        if (service == null)
            throw new InternalServiceException("No connection to authentication-service.");
    }

    public String generateTokenForEmail(String email) {
        return restTemplate.postForObject(authServiceUrl + "/internal/generateTokenForEmail", email, String.class);
    }

    public String retrieveEmailFromToken(String token) {
        return restTemplate.postForObject(authServiceUrl + "/internal/retrieveEmail", token, String.class);
    }

}
