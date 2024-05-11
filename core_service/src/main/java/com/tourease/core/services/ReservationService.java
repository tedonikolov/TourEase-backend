package com.tourease.core.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.core.models.dto.ReservationCreateDTO;
import com.tourease.core.models.dto.UserVO;
import com.tourease.core.services.communication.HotelServiceClient;
import com.tourease.core.services.communication.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationService {
    private final UserServiceClient userServiceClient;
    private final HotelServiceClient hotelServiceClient;

    public void createReservation(ReservationCreateDTO reservationInfo, Long userId) {
        userServiceClient.checkConnection();
        UserVO userVO = userServiceClient.getCustomerDetails(userId);
        if (userVO.regular() == null || userVO.regular().passport() == null || userVO.regular().passport().expired()) {
            throw new CustomException("Invalid user personal info", ErrorCode.Failed);
        }

        hotelServiceClient.checkConnection();
        hotelServiceClient.createReservation(reservationInfo, userVO);
    }
}
