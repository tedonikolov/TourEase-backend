package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Owner;
import com.tourease.hotel.models.mappers.HotelMapper;
import com.tourease.hotel.repositories.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final OwnerService ownerService;
    private final LocationService locationService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void save(HotelCreateVO hotelCreateVO){
        if(hotelCreateVO.id()==0){
            Owner owner = ownerService.findOwnerById(hotelCreateVO.ownerId());
            Hotel hotel = hotelRepository.save(HotelMapper.toEntity(hotelCreateVO, owner));

            hotelCreateVO.location().setId(hotel.getId());
            locationService.save(hotelCreateVO.location());

            kafkaTemplate.send("hotel_service", owner.getEmail(), "Hotel created with name:"+hotelCreateVO.name());
        }else {
            Hotel hotel = findById(hotelCreateVO.id());

            HotelMapper.updateEntity(hotel,hotelCreateVO);
            hotelRepository.save(hotel);
            kafkaTemplate.send("hotel_service", hotel.getOwner().getEmail(), "Hotel updated with name:"+hotelCreateVO.name());
        }
    }

    public Hotel findById(Long id){
        return hotelRepository.findById(id).orElseThrow(()->new CustomException("Hotel not found", ErrorCode.EntityNotFound));
    }
}
