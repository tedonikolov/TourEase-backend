package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.ReservationStatus;

public record ReservationStatusVO(
        Long reservationNumber,
        ReservationStatus status
) {
}
