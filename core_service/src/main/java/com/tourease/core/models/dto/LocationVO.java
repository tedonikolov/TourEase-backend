package com.tourease.core.models.dto;

import java.math.BigDecimal;

public record LocationVO(
        BigDecimal latitude,
        BigDecimal longitude,
        String address,
        String city,
        String country
) {
}
