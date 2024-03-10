package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.TypeVO;
import com.tourease.hotel.models.entities.Bed;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.mappers.TypeMapper;
import com.tourease.hotel.repositories.BedRepository;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.TypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;
    private final HotelRepository hotelRepository;
    private final BedRepository bedRepository;

    public void save(TypeVO typeVO){
        Hotel hotel = hotelRepository.findById(typeVO.hotelId()).get();
        List<Bed> bedList = bedRepository.findAllById(typeVO.beds());
        Type type;

        if(typeVO.id()==0){
            type = TypeMapper.toEntity(typeVO,hotel);
        }else {
            type = typeRepository.findById(typeVO.id()).get();
            TypeMapper.updateEntity(type,typeVO);
        }

        type.setBeds(bedList);
        typeRepository.save(type);
    }

    public void delete(Long id) {
        typeRepository.deleteById(id);
    }
}
