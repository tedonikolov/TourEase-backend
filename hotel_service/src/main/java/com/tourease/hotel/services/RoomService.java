package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.RoomVO;
import com.tourease.hotel.models.dto.response.RoomReservationVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Reservation;
import com.tourease.hotel.models.entities.Room;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.models.enums.RoomStatus;
import com.tourease.hotel.models.mappers.RoomMapper;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.RoomRepository;
import com.tourease.hotel.repositories.TypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public void changeStatus(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new CustomException("Room not found", ErrorCode.EntityNotFound));
        if (room.getStatus() == null)
            room.setStatus(RoomStatus.FREE);
        switch (room.getStatus()) {
            case FREE -> room.setStatus(RoomStatus.CLEANING);
            case CLEANING -> room.setStatus(RoomStatus.MAINTENANCE);
            case MAINTENANCE -> room.setStatus(RoomStatus.FREE);
        }
        roomRepository.save(room);
    }

    public Room getRoomById(Long roomId) {
        return roomRepository.getById(roomId);
    }

    public RoomReservationVO getReservationForRoom(Long roomId, LocalDate date) {
        Room room = roomRepository.getById(roomId);

        Optional<Reservation> info = room.getReservations().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACCOMMODATED
                        || reservation.getStatus() == ReservationStatus.CONFIRMED
                        || reservation.getStatus() == ReservationStatus.ENDING
                        || reservation.getStatus() == ReservationStatus.FINISHED)
                .filter(reservation ->
                (reservation.getCheckIn().toLocalDate().isBefore(date) || reservation.getCheckIn().toLocalDate().isEqual(date))
                        && (reservation.getCheckOut().toLocalDate().isAfter(date) || reservation.getCheckOut().toLocalDate().isEqual(date))
        ).max(Comparator.comparing((Reservation r) -> switch (r.getStatus()) {
            case FINISHED -> 1;
            case CONFIRMED -> 2;
            case ACCOMMODATED -> 3;
            case ENDING -> 4;
            default -> 0;
        }));

        return new RoomReservationVO(room, info.orElse(null), info.map(Reservation::getWorker).orElse(null));
    }

    public List<OffsetDateTime> getTakenDaysForRoom(Long id) {
        Room room = roomRepository.getById(id);

        List<Reservation> reservations = room.getReservations().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACCOMMODATED
                        || reservation.getStatus() == ReservationStatus.CONFIRMED).toList();

        List<OffsetDateTime> offsetDateTimes = new ArrayList<>();
        for (Reservation reservation : reservations) {
            for (OffsetDateTime date = reservation.getCheckIn(); date.isBefore(reservation.getCheckOut()); date = date.plusDays(1)) {
                offsetDateTimes.add(date);
            }
        }

        return offsetDateTimes;
    }
}
