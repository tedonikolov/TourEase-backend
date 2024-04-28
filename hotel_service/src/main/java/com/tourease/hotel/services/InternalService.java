package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.response.DataSet;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.repositories.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InternalService {
    private final HotelRepository hotelRepository;

    public DataSet getDataSet() {
        List<Hotel> hotels = hotelRepository.findAll();


        return new DataSet(
                hotels.stream().map(hotel -> hotel.getLocation().getCountry()).distinct().toList(),
                hotels.stream().map(hotel -> hotel.getLocation().getCity()).distinct().toList(),
                hotels.stream().map(hotel -> hotel.getLocation().getAddress()).distinct().toList(),
                hotels.stream().map(Hotel::getName).distinct().toList()
        );
    }
}
