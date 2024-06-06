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

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private final UserServiceClient userServiceClient;
    private final HotelServiceClient hotelServiceClient;
    private final ReservationRepository reservationRepository;

    public void createReservation(ReservationCreateDTO reservationInfo, Long userId) {
        userServiceClient.checkConnection();
        UserVO userVO = userServiceClient.getCustomerDetails(userId);
        if (userVO.regular() == null || userVO.regular().passport() == null) {
            throw new CustomException("Invalid user personal info", ErrorCode.Failed);
        } else if (userVO.regular().passport().expired()) {
            throw new CustomException("Expired passport", ErrorCode.Failed);
        }

        hotelServiceClient.checkConnection();
        try {
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

        } catch (CustomException e) {
            throw new CustomException("No available rooms", ErrorCode.Failed);
        }
    }

    public void changeReservationStatus(ReservationStatusDTO reservationStatus) {
        reservationRepository.findByReservationNumber(reservationStatus.reservationNumber()).ifPresent((reservation -> {
            reservation.setStatus(reservationStatus.status());
            reservationRepository.save(reservation);
        }));
    }

    public void cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            reservation.get().setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation.get());
            hotelServiceClient.cancelReservation(reservation.get().getReservationNumber());
        }
    }

    public IndexVM<ReservationDTO> getReservations(Long userId, ReservationsFilter reservationsFilter, Integer page, Integer size) {
        Page<Reservation> reservations = reservationRepository.findByUserId(userId, reservationsFilter.getReservationNumber(),
                reservationsFilter.getCreationDate() != null ? OffsetDateTime.of(reservationsFilter.getCreationDate(), LocalTime.MIN, ZoneOffset.UTC) : null,
                reservationsFilter.getCreationDate() != null ? OffsetDateTime.of(reservationsFilter.getCreationDate(), LocalTime.MAX, ZoneOffset.UTC) : null,
                reservationsFilter.getCheckIn() != null ? OffsetDateTime.of(reservationsFilter.getCheckIn(), LocalTime.MIN, ZoneOffset.UTC) : null,
                reservationsFilter.getCheckIn() != null ? OffsetDateTime.of(reservationsFilter.getCheckIn(), LocalTime.MAX, ZoneOffset.UTC) : null,
                reservationsFilter.getStatus(), PageRequest.of(page, size));
        List<ReservationDTO> reservationDTOs = new ArrayList<>();

        for (Reservation reservation : reservations) {
            HotelVO hotel = hotelServiceClient.getHotel(reservation.getReservationNumber());
            if(reservationsFilter.getHotel() != null && Objects.equals(hotel.name(), reservationsFilter.getHotel()))
                reservationDTOs.add(new ReservationDTO(reservation, hotel));
            else if(reservationsFilter.getHotel() == null)
                reservationDTOs.add(new ReservationDTO(reservation, hotel));
        }

        IndexVM indexVM = new IndexVM<>(reservations);
        indexVM.setItems(reservationDTOs);
        return indexVM;
    }

    public Boolean checkReservation(Long reservationNumber) {
        return reservationRepository.findByReservationNumber(reservationNumber).isPresent();
    }

    public void updateReservation(ReservationUpdateVO reservationUpdateVO) {
        reservationRepository.findByReservationNumber(reservationUpdateVO.reservationNumber()).ifPresent((reservation -> {
            reservation.setCheckIn(reservationUpdateVO.checkIn());
            reservation.setCheckOut(reservationUpdateVO.checkOut());
            reservation.setNights(reservationUpdateVO.nights());
            reservation.setPeopleCount(reservationUpdateVO.peopleCount());
            reservation.setPrice(reservationUpdateVO.price());
            reservation.setCurrency(reservationUpdateVO.currency());
            reservationRepository.save(reservation);
        }));
    }
}
