package com.tourease.hotel.models.dto.response;

import java.util.List;
import java.util.Map;

public record RoomMapVO(Map<String,RoomVO> rooms,
                        List<RoomTypeVO> block
                        ) {
}
