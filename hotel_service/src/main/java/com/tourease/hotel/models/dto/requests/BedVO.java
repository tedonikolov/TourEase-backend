package com.tourease.hotel.models.dto.requests;

import java.math.BigDecimal;

public record BedVO(Long id, String name, int people, BigDecimal price, Long hotelId) {
}
