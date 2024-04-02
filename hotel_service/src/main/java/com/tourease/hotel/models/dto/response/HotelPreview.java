package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.enums.Stars;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelPreview{
        private Long hotelId;
        private String country;
        private String city;
        private String address;
        private String name;
        private Stars stars;
        private Set<Type> types;
        private List<String> images;
        private BigDecimal rating;
        private Long numberOfRates;

        public HotelPreview(Hotel hotel, Set<Type> types) {
            this.hotelId = hotel.getId();
            this.country = hotel.getLocation().getCountry();
            this.city = hotel.getLocation().getCity();
            this.address = hotel.getLocation().getAddress();
            this.name = hotel.getName();
            this.stars = hotel.getStars();
            this.types = types;
            this.rating = hotel.getRating() != null ? hotel.getRating().getRating() : null;
            this.numberOfRates = hotel.getRating() != null ? hotel.getRating().getNumberOfRates() : null;
        }

    public void setImages(List<String> images) {
        this.images = images.size() < 5 ? images : images.subList(0, 5);
    }
}
