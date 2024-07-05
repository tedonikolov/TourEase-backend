package com.tourease.hotel.models.dto.requests;

import java.time.LocalDate;
import java.util.List;

public record TakenDaysForRoom(
        List<LocalDate> checkInDates,
        List<LocalDate> checkOutDates
) {
}
