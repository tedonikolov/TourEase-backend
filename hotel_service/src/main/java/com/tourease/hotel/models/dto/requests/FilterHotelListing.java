package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.FacilityEnum;
import com.tourease.hotel.models.enums.MealType;
import com.tourease.hotel.models.enums.Stars;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FilterHotelListing {
    private String country;
    private String city;
    private String name;
    private Stars stars;
    private List<FacilityEnum> facilities;
    private MealType mealType;
    private Integer people;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int pageNumber;
    private Currency currency;

    public void decodeURL() {
        if (facilities == null || facilities.isEmpty()) {
            facilities = null;
        }
        if (name == null || name.isEmpty()) {
            name = null;
        }
        if (country == null || country.isEmpty()) {
            country = null;
        }
        if (city == null || city.isEmpty()) {
            city = null;
        }
        if (name != null) {
            name = URLDecoder.decode(name, StandardCharsets.UTF_8);
        }
        if (country != null) {
            country = URLDecoder.decode(country, StandardCharsets.UTF_8);
        }
        if (city != null) {
            city = URLDecoder.decode(city, StandardCharsets.UTF_8);
        }
    }
}
