package com.tourease.core.models.dto;

import java.util.List;

public record DataSet(
        List<String> countries,
        List<String> cities,
        List<String> addresses,
        List<String> names
) {
}
