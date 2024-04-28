package com.tourease.hotel.models.dto.response;

import java.util.List;

public record DataSet(
        List<String> countries,
        List<String> cities,
        List<String> addresses,
        List<String> names
) {
}
