package com.tourease.user.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class HotelServiceClient {
    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    private final String hotelAppName = "CONFIGURATION-SERVICE";
    private final String hotelServiceUrl = "http://hotel-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(hotelAppName);
        if (service == null)
            throw new InternalServiceException("No connection to hotel-service.");
    }

    public void createHotelOwner(Long id, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user", email);
        restTemplate.postForObject(hotelServiceUrl + "/internal/createOwner?id=" + id, new HttpEntity<>(headers), void.class);
    }

}
