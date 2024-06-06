package com.tourease.hotel.models.dto.response;

public record BedVO(String name,
                    String name_with_count,
                    Integer bed_type,
                    int count) {
}
