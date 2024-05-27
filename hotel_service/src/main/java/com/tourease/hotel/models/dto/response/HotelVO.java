package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.enums.Stars;

public record HotelVO(
        Long id,
        String name,
        Stars stars,
        String country,
        String city,
        String address
) {
}
