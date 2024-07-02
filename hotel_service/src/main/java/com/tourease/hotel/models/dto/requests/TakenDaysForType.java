package com.tourease.hotel.models.dto.requests;

import java.time.LocalDate;
import java.util.List;

public record TakenDaysForType(
        List<LocalDate> checkInDates,
        List<LocalDate> checkOutDates
) {
}
