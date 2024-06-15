package com.tourease.email.models.dto;

public record ReservationConfirmationVO(
        String email,
        String country,
        ReservationVO reservationVO
) {
}
