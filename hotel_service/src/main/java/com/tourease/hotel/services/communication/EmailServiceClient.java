package com.tourease.hotel.services.communication;

import com.tourease.hotel.models.dto.requests.ReservationConfirmationVO;
import com.tourease.hotel.models.dto.requests.ReservationDeclinedVO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EmailServiceClient {
    private final RestTemplate defaultRestTemplate;

    private final String userServiceUrl = "http://email-service";

    public void sendReservationConfirmation(ReservationConfirmationVO reservationConfirmationVO) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/sendReservationConfirmation", new HttpEntity<>(reservationConfirmationVO));
    }

    public void sendDeclinedReservation(ReservationDeclinedVO reservationDeclinedVO) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/sendDeclinedReservation", new HttpEntity<>(reservationDeclinedVO));
    }
}
