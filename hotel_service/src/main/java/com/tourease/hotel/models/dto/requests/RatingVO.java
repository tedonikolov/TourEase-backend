package com.tourease.hotel.models.dto.requests;

public record RatingVO(
        Long hotelId,
        Double rating,
        String comment
){}
