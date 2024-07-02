package com.tourease.core.models.dto;

import java.time.LocalDate;
import java.util.List;

public record TakenDaysForType(
        List<LocalDate> checkInDates,
        List<LocalDate> checkOutDates
) {
}
