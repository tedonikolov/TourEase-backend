package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Reservation;
import com.tourease.hotel.models.enums.ReservationStatus;

public record SchemaReservationsVO(Long reservationId,
                                   Long roomId,
                                   ReservationStatus status
) {
    public SchemaReservationsVO(Reservation reservation) {
        this(reservation.getId(), reservation.getRoom()!=null ? reservation.getRoom().getId() : null, reservation.getStatus());
    }
}
