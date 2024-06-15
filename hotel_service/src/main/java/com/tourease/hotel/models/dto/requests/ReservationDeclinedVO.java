package com.tourease.hotel.models.dto.requests;

public record ReservationDeclinedVO(
        Long reservationNumber,
        String email,
        String name
) {
}
