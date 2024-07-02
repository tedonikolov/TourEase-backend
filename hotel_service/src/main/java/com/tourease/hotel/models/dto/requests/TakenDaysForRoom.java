package com.tourease.hotel.models.dto.requests;

import java.time.OffsetDateTime;
import java.util.List;

public record TakenDaysForRoom(
        List<OffsetDateTime> checkInDates,
        List<OffsetDateTime> checkOutDates
) {
}
