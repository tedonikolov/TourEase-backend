package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.FilterHotelListing;
import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.models.dto.response.HotelPreview;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.mappers.HotelMapper;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final ImageRepository imageRepository;
    private final OwnerService ownerService;
    private final LocationService locationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void save(HotelCreateVO hotelCreateVO) {
        if (hotelCreateVO.id() == 0) {
            Owner owner = ownerService.findOwnerById(hotelCreateVO.ownerId());
            Hotel hotel = hotelRepository.save(HotelMapper.toEntity(hotelCreateVO, owner));

            hotelCreateVO.location().setId(hotel.getId());
            locationService.save(hotelCreateVO.location());

            kafkaTemplate.send("hotel_service", owner.getEmail(), "Hotel created with name:" + hotelCreateVO.name());
        } else {
            Hotel hotel = findById(hotelCreateVO.id());

            HotelMapper.updateEntity(hotel, hotelCreateVO);
            hotelRepository.save(hotel);
            kafkaTemplate.send("hotel_service", hotel.getOwner().getEmail(), "Hotel updated with name:" + hotelCreateVO.name());
        }
    }

    public Hotel findById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new CustomException("Hotel not found", ErrorCode.EntityNotFound));
    }

    public List<HotelPreview> listing(FilterHotelListing filterHotelListing) {
        List<Hotel> hotelsList = hotelRepository.findHotelByFilter(filterHotelListing.country(),
                filterHotelListing.city(), filterHotelListing.address(),
               filterHotelListing.name(), filterHotelListing.stars(),
               filterHotelListing.facilities());

        List<HotelPreview> hotels = new ArrayList<>();

        for (Hotel hotel : hotelsList) {
            List<Image> images = imageRepository.findByHotel_Id(hotel.getId());

            Set<Type> types = hotel.getTypes().stream().filter(type -> filterTypePrice(type, filterHotelListing) && filterTypePeople(type, filterHotelListing)).collect(Collectors.toSet());

            if (!types.isEmpty()) {
                HotelPreview hotelPreview = new HotelPreview(hotel, types, images.size() < 5 ? images : images.subList(0, 5));
                hotels.add(hotelPreview);
            }
        }

        return hotels;
    }

    private boolean filterTypePrice(Type type, FilterHotelListing filter) {
        if (filter.fromPrice() != null && filter.toPrice() != null && type.getPrice().doubleValue() >= filter.fromPrice().doubleValue() && type.getPrice().doubleValue() <= filter.toPrice().doubleValue())
            return true;
        else {
            if (filter.fromPrice() != null && filter.toPrice() == null && type.getPrice().doubleValue() >= filter.fromPrice().doubleValue())
                return true;
            else if (filter.toPrice() != null && filter.fromPrice() == null && type.getPrice().doubleValue() <= filter.toPrice().doubleValue())
                return true;
            else
                return filter.fromPrice() == null && filter.toPrice() == null;
        }
    }

    private boolean filterTypePeople(Type type, FilterHotelListing filter) {
        if (filter.people() == null)
            return true;
        else
            return type.getBeds().stream().mapToInt(Bed::getPeople).sum() >= filter.people();
    }
}
