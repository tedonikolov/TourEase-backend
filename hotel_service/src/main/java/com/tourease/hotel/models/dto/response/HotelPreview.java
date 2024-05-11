package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.Stars;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelPreview{
        private Long hotelId;
        private Location location;
        private String name;
        private Stars stars;
        private Set<Type> types;
        private Set<Meal> meals;
        private List<String> images;
        private Set<Facility> facilities;
        private BigDecimal rating;
        private Long numberOfRates;
        private Integer people;
        private LocalDate openingDate;
        private LocalDate closingDate;

        public HotelPreview(Hotel hotel, Set<Meal> meals, Set<Type> types, int people) {
            this.hotelId = hotel.getId();
            this.location = hotel.getLocation();
            this.name = hotel.getName();
            this.stars = hotel.getStars();
            this.types = types;
            this.meals = meals;
            this.facilities = hotel.getFacilities();
            this.rating = hotel.getRating() != null ? hotel.getRating().getRating() : null;
            this.numberOfRates = hotel.getRating() != null ? hotel.getRating().getNumberOfRates() : null;
            this.people = people;
            this.openingDate = hotel.getOpeningDate();
            this.closingDate = hotel.getClosingDate();
        }

    public void setImages(List<String> images) {
        this.images = images.size() < 5 ? images : images.subList(0, 5);
    }
}
