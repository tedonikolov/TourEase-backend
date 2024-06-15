package com.tourease.email.models.dto;

public record ReservationDeclinedVO(
        Long reservationNumber,
        String email,
        String name
) {
}
