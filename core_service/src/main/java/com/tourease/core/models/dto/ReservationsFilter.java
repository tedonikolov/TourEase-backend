package com.tourease.core.models.dto;

import com.tourease.core.models.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ReservationsFilter {
    private String hotel;
    private Long reservationNumber;
    private ReservationStatus status;
    private LocalDate creationDate;
    private LocalDate checkIn;
}
