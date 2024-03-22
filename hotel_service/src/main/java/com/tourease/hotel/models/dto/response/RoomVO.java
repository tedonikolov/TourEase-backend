package com.tourease.hotel.models.dto.response;

import java.util.List;

public record RoomVO(
        List<FacilityVO> facilities,
        List<PhotosVO> photos,
        List<BedConfiguration> bed_configurations
) {
}
