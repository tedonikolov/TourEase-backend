package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.entities.Bed;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public record TypeVO(Long id, String name, BigDecimal price, Currency currency, List<Long> beds, Long hotelId) {
    public TypeVO(Type type) {
        this(type.getId(), type.getName(), type.getPrice(), type.getCurrency(), type.getBeds().stream().map(Bed::getId).toList(), type.getHotel().getId());
    }
}
