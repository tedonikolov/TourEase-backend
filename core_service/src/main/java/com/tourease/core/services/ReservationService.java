package com.tourease.core.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.core.repositories.ReservationRepository;
import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.*;
import com.tourease.core.models.entities.Reservation;
import com.tourease.core.models.enums.ReservationStatus;
import com.tourease.core.services.communication.HotelServiceClient;
import com.tourease.core.services.communication.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final UserServiceClient userServiceClient;
    private final HotelServiceClient hotelServiceClient;
    private final ReservationRepository reservationRepository;

    public void createReservation(ReservationCreateDTO reservationInfo, Long userId) {
        userServiceClient.checkConnection();
        UserVO userVO = userServiceClient.getCustomerDetails(userId);
        if (userVO.regular() == null || userVO.regular().passport() == null || userVO.regular().passport().expired()) {
            throw new CustomException("Invalid user personal info", ErrorCode.Failed);
        }

        hotelServiceClient.checkConnection();
        Long number = hotelServiceClient.createReservation(reservationInfo, userVO);

        reservationRepository.save(Reservation.builder()
                .reservationNumber(number)
                .userId(userId)
                .nights(reservationInfo.nights())
                .peopleCount(reservationInfo.peopleCount())
                .checkIn(reservationInfo.checkIn())
                .checkOut(reservationInfo.checkOut())
                .price(reservationInfo.price())
                .currency(reservationInfo.currency())
                .status(ReservationStatus.PENDING)
                .build());
    }

    public void changeReservationStatus(ReservationStatusDTO reservationStatus) {
        reservationRepository.findByReservationNumber(reservationStatus.reservationNumber()).ifPresent((reservation -> {
            reservation.setStatus(reservationStatus.status());
            reservationRepository.save(reservation);
        }));
    }

    public void cancelReservation(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent((reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        }));
        hotelServiceClient.cancelReservation(reservationId);
    }

    public IndexVM<ReservationDTO> getReservations(Long userId, Integer page, Integer size) {
        Page<Reservation> reservations = reservationRepository.findByUserId(userId, PageRequest.of(page, size));

        List<ReservationDTO> reservationDTOs = new ArrayList<>();

        for (Reservation reservation : reservations) {
            HotelVO hotel = hotelServiceClient.getHotel(reservation.getReservationNumber());
            reservationDTOs.add(new ReservationDTO(reservation, hotel));
        }

        IndexVM indexVM = new IndexVM<>(reservations);
        indexVM.setItems(reservationDTOs);
        return indexVM;
    }
}
