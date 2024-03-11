package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.RoomVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Room;

public class RoomMapper {
    public static Room toEntity(RoomVO vo, Hotel hotel){
        return Room.builder()
                .name(vo.name())
                .hotel(hotel)
                .build();
    }

    public static void updateEntity(Room entity, RoomVO vo){
        entity.setName(vo.name());
    }
}
