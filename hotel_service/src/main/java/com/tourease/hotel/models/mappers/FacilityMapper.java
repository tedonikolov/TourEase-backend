package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.FacilityVO;
import com.tourease.hotel.models.entities.Facility;
import com.tourease.hotel.models.entities.Hotel;

public class FacilityMapper {
    public static Facility toEntity(FacilityVO vo, Hotel hotel){
        return Facility.builder()
                .name(vo.name())
                .paid(vo.paid())
                .price(vo.price())
                .currency(vo.currency())
                .hotel(hotel)
                .build();
    }

    public static void updateEntity(Facility entity,FacilityVO vo){
        entity.setPaid(vo.paid());
        entity.setPrice(vo.price());
        entity.setCurrency(vo.currency());
    }
}
