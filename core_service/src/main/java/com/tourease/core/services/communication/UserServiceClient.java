package com.tourease.core.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.core.models.dto.PaymentChangeReservationVO;
import com.tourease.core.models.dto.UserVO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class UserServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String userAppName = "USER-SERVICE";
    private final String userServiceUrl = "http://user-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(userAppName);
        if (service == null)
            throw new InternalServiceException("No connection to user-service.");
    }

    public UserVO getCustomerDetails(Long id) {
        return defaultRestTemplate.getForEntity(userServiceUrl + "/internal/getCustomerDetails/"+id, UserVO.class).getBody();
    }

    public void sendPaymentChangeReservation(PaymentChangeReservationVO paymentChangeReservationVO) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/sendPaymentChangeReservation", new HttpEntity<>(paymentChangeReservationVO));
    }

}
