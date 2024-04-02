package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.FacilityEnum;
import com.tourease.hotel.models.enums.Stars;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record FilterHotelListing(String country,
                                 String city,
                                 String address,
                                 String name,
                                 Stars stars,
                                 List<FacilityEnum> facilities,
                                 Integer people,
                                 BigDecimal fromPrice,
                                 BigDecimal toPrice,
                                 OffsetDateTime fromDate,
                                 OffsetDateTime toDate,
                                 int pageNumber) {
}
