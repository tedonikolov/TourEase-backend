package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Stars;

public record HotelVO(
        Long id,
        String name,
        Stars stars,
        String country,
        String city,
        String address
) {
}
