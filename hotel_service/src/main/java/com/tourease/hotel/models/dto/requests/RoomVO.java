package com.tourease.hotel.models.dto.requests;

import java.util.List;

public record RoomVO (Long id, String name, List<Long> types, Long hotelId){
}
