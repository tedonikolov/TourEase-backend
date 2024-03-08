package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.BedVO;
import com.tourease.hotel.models.entities.Bed;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.mappers.BedMapper;
import com.tourease.hotel.repositories.BedRepository;
import com.tourease.hotel.repositories.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BedService {
    private final BedRepository bedRepository;
    private final HotelRepository hotelRepository;

    public void save(BedVO bedVO){
        Hotel hotel = hotelRepository.findById(bedVO.hotelId()).get();
        if(bedVO.id()==0){
            Bed bed = BedMapper.toEntity(bedVO,hotel);
            bedRepository.save(bed);
        }else {
            Bed bed = bedRepository.findById(bedVO.id()).get();
            BedMapper.updateEntity(bed,bedVO);
            bedRepository.save(bed);
        }
    }

    public void delete(Long id) {
        bedRepository.deleteById(id);
    }
}
