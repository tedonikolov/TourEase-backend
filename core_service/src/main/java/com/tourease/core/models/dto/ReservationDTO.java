package com.tourease.core.models.dto;

import com.tourease.core.models.entities.Rating;
import com.tourease.core.models.entities.Reservation;
import com.tourease.core.models.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ReservationDTO {
    private Long id;
    private Long reservationNumber;
    private OffsetDateTime creationDate;
    private OffsetDateTime checkIn;
    private OffsetDateTime checkOut;
    private Integer nights;
    private String price;
    private Integer peopleCount;
    private String currency;
    private ReservationStatus status;
    private boolean paid;
    private HotelVO hotel;
    private Rating rating;

    public ReservationDTO(Reservation reservation, HotelVO hotelVO) {
        this.id = reservation.getId();
        this.reservationNumber = reservation.getReservationNumber();
        this.creationDate = reservation.getCreationDate();
        this.checkIn = reservation.getCheckIn();
        this.checkOut = reservation.getCheckOut();
        this.nights = reservation.getNights();
        this.price = reservation.getPrice().toString();
        this.peopleCount = reservation.getPeopleCount();
        this.currency = reservation.getCurrency().name();
        this.status = reservation.getStatus();
        this.paid = reservation.isPaid();
        this.hotel = hotelVO;
        this.rating = reservation.getRating();
    }
}
