package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.models.entities.Reservation;
import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservationListing(
        Long id,
        Long reservationId,
        Long roomId,
        String roomName,
        String roomType,
        OffsetDateTime checkIn,
        OffsetDateTime checkOut,
        int nights,
        BigDecimal price,
        Currency currency,
        List<Customer> customers,
        String workerName
) {
    public ReservationListing(Reservation reservation, BigDecimal price, Currency currency, List<Customer> customers) {
        this(reservation.getId(), reservation.getReservationNumber(), reservation.getRoom().getId(), reservation.getRoom().getName(), reservation.getRoom().getName(), reservation.getCheckIn(), reservation.getCheckOut(), reservation.getNights(), price, currency, customers, reservation.getWorker().getFullName());
    }
}
