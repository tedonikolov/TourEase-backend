package com.tourease.hotel.models.dto.requests;

import java.time.LocalDate;

public record HotelWorkingPeriodVO(Long hotelId,
                                   LocalDate openingDate,
                                   LocalDate closingDate) {
}
