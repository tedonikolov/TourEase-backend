package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Facility;
import com.tourease.core.models.enums.Stars;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record HotelPreview(
        Long hotelId,
        LocationVO location,
        String name,
        Stars stars,
        Set<TypeVO>types,
        Set<MealVO> meals,
        List<String> images,
        List<Facility> facilities,
        BigDecimal rating,
        Long numberOfRates,
        Integer people
) {
}
