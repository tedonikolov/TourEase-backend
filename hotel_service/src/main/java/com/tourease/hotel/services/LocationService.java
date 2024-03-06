package com.tourease.hotel.services;

import com.tourease.hotel.models.entities.Location;
import com.tourease.hotel.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void save(Location location){
        locationRepository.save(location);
    }
}
