package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.models.entities.Reservation;
import com.tourease.hotel.models.entities.Room;
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
        OffsetDateTime checkIn,
        OffsetDateTime checkOut,
        int nights,
        BigDecimal price,
        Currency currency,
        List<Customer> customers,
        String workerName
) {
    public ReservationListing(Reservation reservation, BigDecimal price, Currency currency, List<Customer> customers) {
        this(reservation.getId(), reservation.getReservationNumber(), reservation.getStatus(), reservation.getRoom(), reservation.getCheckIn(), reservation.getCheckOut(), reservation.getNights(), price, currency, customers, reservation.getWorker().getFullName());
    }
}
