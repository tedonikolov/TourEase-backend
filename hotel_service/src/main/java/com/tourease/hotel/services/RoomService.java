package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.RoomVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Room;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.mappers.RoomMapper;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.RoomRepository;
import com.tourease.hotel.repositories.TypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final TypeRepository typeRepository;

    public void save(RoomVO roomVO) {
        Hotel hotel = hotelRepository.findById(roomVO.hotelId()).get();
        List<Type> types = typeRepository.findAllById(roomVO.types());
        Room room;

        if (roomVO.id() == 0) {
            if (roomRepository.findByNameAndHotel_Id(roomVO.name(), roomVO.hotelId()).isPresent()) {
                throw new CustomException("Room exist", ErrorCode.AlreadyExists);
            }
            room = RoomMapper.toEntity(roomVO, hotel);
        } else {
            room = roomRepository.findById(roomVO.id()).get();
            RoomMapper.updateEntity(room, roomVO);
        }

        room.setTypes(types);
        roomRepository.save(room);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new CustomException("Room not found", ErrorCode.EntityNotFound));
    }
}
