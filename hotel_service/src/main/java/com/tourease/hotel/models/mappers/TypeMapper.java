package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.TypeVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Type;

public class TypeMapper {
    public static Type toEntity(TypeVO vo, Hotel hotel){
        return Type.builder()
                .name(vo.name())
                .price(vo.price())
                .currency(vo.currency())
                .hotel(hotel)
                .build();
    }

    public static void updateEntity(Type entity,TypeVO vo){
        entity.setName(vo.name());
        entity.setPrice(vo.price());
        entity.setCurrency(vo.currency());
    }
}
