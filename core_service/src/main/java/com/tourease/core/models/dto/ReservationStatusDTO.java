package com.tourease.core.models.dto;

import com.tourease.core.models.enums.ReservationStatus;

public record ReservationStatusDTO(
        Long reservationNumber,
        ReservationStatus status
) {
}
