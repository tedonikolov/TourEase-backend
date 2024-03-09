package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.BedVO;
import com.tourease.hotel.models.entities.Bed;
import com.tourease.hotel.models.entities.Hotel;

public class BedMapper {
    public static Bed toEntity(BedVO vo, Hotel hotel){
        return Bed.builder()
                .name(vo.name())
                .people(vo.people())
                .price(vo.price())
                .currency(vo.currency())
                .hotel(hotel)
                .build();
    }

    public static void updateEntity(Bed entity,BedVO vo){
        entity.setName(vo.name());
        entity.setPeople(vo.people());
        entity.setPrice(vo.price());
        entity.setCurrency(vo.currency());
    }
}
