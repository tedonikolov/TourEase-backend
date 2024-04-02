package com.tourease.hotel.models.dto.requests;

import java.time.OffsetDateTime;

public record ReservationCreateDTO(CustomerDTO customer,
                                   Long roomId,
                                   Long typeId,
                                   OffsetDateTime checkIn,
                                   OffsetDateTime checkOut) {
}