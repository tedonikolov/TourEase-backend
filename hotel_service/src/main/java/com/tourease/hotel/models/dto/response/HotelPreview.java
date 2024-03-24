package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Image;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.enums.Stars;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record HotelPreview(Long hotelId,
                           String country,
                           String city,
                           String address,
                           String name,
                           Stars stars,
                           Set<Type> types,
                           List<String> images,
                           BigDecimal rating,
                           Long numberOfRates) {
    public HotelPreview(Hotel hotel, Set<Type> types, List<Image> images) {
        this(hotel.getId(), hotel.getLocation().getCountry(), hotel.getLocation().getCity(),
                hotel.getLocation().getAddress(), hotel.getName(), hotel.getStars(),
                types, images.stream().map(Image::getUrl).toList(),
                hotel.getRating() != null ? hotel.getRating().getRating() : null,
                hotel.getRating() !=null ? hotel.getRating().getNumberOfRates() : null);
    }
}
