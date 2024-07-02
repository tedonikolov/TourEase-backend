package com.tourease.core.models.dto;

import com.tourease.core.models.enums.Currency;
import com.tourease.core.models.enums.Facility;
import com.tourease.core.models.enums.MealType;
import com.tourease.core.models.enums.Stars;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FilterHotelListing {
    private String country;
    private String city;
    private String name;
    private Stars stars;
    private List<Facility> facilities;
    private MealType mealType;
    private Integer people;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int pageNumber;
    private Currency currency;
}
