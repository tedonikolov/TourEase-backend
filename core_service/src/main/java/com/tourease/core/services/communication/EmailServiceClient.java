package com.tourease.core.services.communication;

import com.tourease.core.models.dto.PaymentChangeReservationVO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EmailServiceClient {
    private final RestTemplate defaultRestTemplate;

    private final String emailServiceUrl = "http://email-service";

    public void sendPaymentChangeReservation(PaymentChangeReservationVO paymentChangeReservationVO) {
        defaultRestTemplate.postForLocation(emailServiceUrl + "/internal/sendPaymentChangeReservation", new HttpEntity<>(paymentChangeReservationVO));
    }
}
