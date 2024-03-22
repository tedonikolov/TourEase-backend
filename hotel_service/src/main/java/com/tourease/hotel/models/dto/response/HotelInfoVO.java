package com.tourease.hotel.models.dto.response;

import java.util.List;

public record HotelInfoVO(String address,
                          String city,
                          String country_trans,
                          List<FacilityVO> property_highlight_strip) {
}
