package com.tourease.hotel.models.dto.requests;

public record ReservationConfirmationVO(
        String email,
        String country,
        ReservationVO reservationVO
) {
}
