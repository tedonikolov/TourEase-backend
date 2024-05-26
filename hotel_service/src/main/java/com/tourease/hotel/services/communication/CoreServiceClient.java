package com.tourease.hotel.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.hotel.models.dto.requests.ReservationStatusVO;
import com.tourease.hotel.models.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CoreServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String userAppName = "CORE-SERVICE";
    private final String userServiceUrl = "http://core-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(userAppName);
        if (service == null)
            throw new InternalServiceException("No connection to core-service.");
    }

    public void changeStatus(Long reservationNumber, ReservationStatus status) {
        defaultRestTemplate.put(userServiceUrl + "/internal/reservation/changeStatus", new HttpEntity<>(new ReservationStatusVO(reservationNumber, status)), Long.class);
    }
}
