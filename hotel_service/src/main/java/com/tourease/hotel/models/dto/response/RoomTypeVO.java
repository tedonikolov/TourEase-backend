package com.tourease.hotel.models.dto.response;

public record RoomTypeVO (
        String room_id,
        String room_name,
        Integer room_count,
        Integer nr_adults,
        Integer nr_children
) {
}
