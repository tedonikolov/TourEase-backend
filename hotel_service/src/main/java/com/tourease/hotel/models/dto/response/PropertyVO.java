package com.tourease.hotel.models.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record PropertyVO(BigDecimal latitude,
                         BigDecimal longitude,
                         String name,
                         BigDecimal reviewScore,
                         Long reviewCount,
                         List<String> photoUrls,
                         int propertyClass) {
}
