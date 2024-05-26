package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Stars;

public record HotelVO(
        String name,
        Stars stars,
        String country,
        String city,
        String address
) {
}
