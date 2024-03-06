package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Owner;

public class HotelMapper {
    public static Hotel toEntity(HotelCreateVO hotelCreateVO, Owner owner){
        return Hotel.builder()
                .name(hotelCreateVO.name())
                .stars(hotelCreateVO.stars())
                .owner(owner)
                .build();
    }

    public static void updateEntity(Hotel hotel, HotelCreateVO vo){
        hotel.setName(vo.name());
        hotel.setStars(vo.stars());
        hotel.getLocation().setCity(vo.location().getCity());
        hotel.getLocation().setCountry(vo.location().getCountry());
        hotel.getLocation().setAddress(vo.location().getAddress());
        hotel.getLocation().setLongitude(vo.location().getLongitude());
        hotel.getLocation().setLatitude(vo.location().getLatitude());
    }
}
