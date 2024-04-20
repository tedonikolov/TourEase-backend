package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservationListing(
        Long id,
        Long reservationNumber,
        ReservationStatus status,
        Room room,
        Type type,
        int people,
        OffsetDateTime checkIn,
        OffsetDateTime checkOut,
        int nights,
        BigDecimal price,
        Currency currency,
        List<Customer> customers,
        String workerName
) {
    public ReservationListing(Reservation reservation, BigDecimal price, Currency currency, List<Customer> customers) {
        this(reservation.getId(), reservation.getReservationNumber(), reservation.getStatus(), reservation.getRoom(), reservation.getType(), reservation.getType().getBeds().stream().map(Bed::getPeople).reduce(0, Integer::sum), reservation.getCheckIn(), reservation.getCheckOut(), reservation.getNights(), price, currency, customers, reservation.getWorker().getFullName());
    }
}
