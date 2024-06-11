package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.entities.Location;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.Stars;

public record HotelCreateVO(Long id, String name, Currency currency, Stars stars, Location location, Long ownerId) {
}
