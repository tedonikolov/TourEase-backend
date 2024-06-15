package com.tourease.user.models.dto.request;

public record ReservationConfirmationVO(
        String email,
        String country,
        ReservationVO reservationVO
) {
}
