package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.FacilityEnum;

import java.math.BigDecimal;

public record FacilityVO(Long id, FacilityEnum name, boolean paid, BigDecimal price, Long hotelId) {
}
