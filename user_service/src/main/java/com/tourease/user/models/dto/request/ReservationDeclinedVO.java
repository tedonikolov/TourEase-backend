package com.tourease.user.models.dto.request;

public record ReservationDeclinedVO(
        Long reservationNumber,
        String email,
        String name
) {
}
