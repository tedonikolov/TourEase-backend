package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.FacilityVO;
import com.tourease.hotel.models.entities.Facility;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.mappers.FacilityMapper;
import com.tourease.hotel.repositories.FacilityRepository;
import com.tourease.hotel.repositories.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final HotelRepository hotelRepository;

    public void save(FacilityVO facilityVO){
        Hotel hotel = hotelRepository.findById(facilityVO.hotelId()).get();
        if(facilityVO.id()==0){
            if(facilityRepository.findByName(facilityVO.name()).isPresent()){
                throw new CustomException("Facility exist", ErrorCode.AlreadyExists);
            }
            Facility facility = FacilityMapper.toEntity(facilityVO,hotel);
            facilityRepository.save(facility);
        }else {
            Facility facility = facilityRepository.findById(facilityVO.id()).get();
            FacilityMapper.updateEntity(facility,facilityVO);
            facilityRepository.save(facility);
        }
    }

    public void delete(Long id) {
        facilityRepository.deleteById(id);
    }
}
