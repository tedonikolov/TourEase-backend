package com.tourease.core.models.dto;

public record RatingVO (
        Long hotelId,
        Double rating,
        String comment
){}
