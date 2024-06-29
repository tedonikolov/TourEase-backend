package com.tourease.hotel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tourease.configuration.exception.CustomException;
import com.tourease.hotel.models.dto.requests.RoomVO;
import com.tourease.hotel.models.dto.response.FreeRoomCountVO;
import com.tourease.hotel.models.dto.response.RoomReservationVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.models.enums.RoomStatus;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.RoomRepository;
import com.tourease.hotel.services.RoomService;
import com.tourease.hotel.services.TypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private TypeService typeService;

    @InjectMocks
    private RoomService roomService;

    private RoomVO roomVO;
    private Room room;
    private Hotel hotel;
    private Type type;
    private List<Type> types;
    private List<Room> rooms;
    private List<Room> takenRooms;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = new Hotel();
        hotel.setId(1L);

        type = new Type();
        type.setId(1L);
        type.setName("Standard");
        Bed bed = new Bed();
        bed.setPeople(2);
        type.setBeds(List.of(bed));
        types = new ArrayList<>();
        types.add(type);

        roomVO = new RoomVO(0L, "Room 101", List.of(1L), 1L);
        room = new Room("Room 101", RoomStatus.FREE, types, hotel);
        Room room2 = new Room("Room 102", RoomStatus.FREE, List.of(type), hotel);

        rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room2);

        takenRooms = new ArrayList<>();
    }

    @Test
    void testSaveNewRoom() {
        when(hotelRepository.findById(roomVO.hotelId())).thenReturn(Optional.of(hotel));
        when(typeService.findAllById(roomVO.types())).thenReturn(types);
        when(roomRepository.findByNameAndHotel_Id(roomVO.name(), roomVO.hotelId())).thenReturn(Optional.empty());

        roomService.save(roomVO);

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testSaveExistingRoom() {
        RoomVO existingRoomVO = new RoomVO(1L, "Room 101", List.of(1L), 1L);
        when(hotelRepository.findById(existingRoomVO.hotelId())).thenReturn(Optional.of(hotel));
        when(typeService.findAllById(existingRoomVO.types())).thenReturn(types);
        when(roomRepository.findById(existingRoomVO.id())).thenReturn(Optional.of(room));

        roomService.save(existingRoomVO);

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testSaveExistingRoomThrowsException() {
        when(hotelRepository.findById(roomVO.hotelId())).thenReturn(Optional.of(hotel));
        when(typeService.findAllById(roomVO.types())).thenReturn(types);
        when(roomRepository.findByNameAndHotel_Id(roomVO.name(), roomVO.hotelId())).thenReturn(Optional.of(room));

        assertThrows(CustomException.class, () -> roomService.save(roomVO));
    }

    @Test
    void testDeleteRoom() {
        roomService.delete(1L);

        verify(roomRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room foundRoom = roomService.findById(1L);

        assertNotNull(foundRoom);
        assertEquals(room.getName(), foundRoom.getName());
    }

    @Test
    void testFindByIdThrowsException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> roomService.findById(1L));
    }

    @Test
    void testChangeStatus() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.changeStatus(1L);

        assertEquals(RoomStatus.CLEANING, room.getStatus());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testGetReservationForRoom() {
        LocalDate date = LocalDate.now();
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setCheckIn(date.atStartOfDay().atOffset(ZoneOffset.UTC));
        reservation.setCheckOut(date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC));

        room.getReservations().add(reservation);
        when(roomRepository.getById(1L)).thenReturn(room);

        RoomReservationVO reservationVO = roomService.getReservationForRoom(1L, date);

        assertNotNull(reservationVO);
        assertEquals(reservation, reservationVO.reservation());
    }

    @Test
    void testFindByIdAndType() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(typeService.findById(1L)).thenReturn(type);

        Room foundRoom = roomService.findByIdAndType(1L, 1L, 2);

        assertNotNull(foundRoom);
        assertEquals(room.getName(), foundRoom.getName());
    }

    @Test
    void testFindByIdAndTypeThrowsException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(typeService.findById(1L)).thenReturn(type);

        assertThrows(CustomException.class, () -> roomService.findByIdAndType(1L, 1L, 3));
    }

    @Test
    void testGetTakenDaysForRoom() {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.CONFIRMED);
        OffsetDateTime checkIn = OffsetDateTime.now().minusDays(1);
        OffsetDateTime checkOut = OffsetDateTime.now().plusDays(1);
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        room.getReservations().add(reservation);

        when(roomRepository.getById(1L)).thenReturn(room);

        List<OffsetDateTime> takenDays = roomService.getTakenDaysForRoom(1L);

        assertNotNull(takenDays);
        assertEquals(2, takenDays.size());
    }

    @Test
    void testGetFreeRoomsBetweenDateByTypeId() {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(5);
        List<Room> freeRooms = List.of(room);

        when(roomRepository.findAllFreeByHotelBetweenDateAndType(1L, 1L, fromDate, fromDate.plusDays(1), toDate, toDate.plusDays(1)))
                .thenReturn(freeRooms);

        List<Room> result = roomService.getFreeRoomsBetweenDateByTypeId(1L, 1L, fromDate, toDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(room.getName(), result.get(0).getName());
    }

    @Test
    void testGetFreeRoomCountByDatesForHotel() {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(5);

        when(roomRepository.findAllByHotel_Id(1L)).thenReturn(rooms);
        when(roomRepository.findAllFinishedByHotelForDate(anyLong(), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(roomRepository.findAllTakenByHotelForDate(anyLong(), any(LocalDate.class))).thenReturn(Collections.emptyList());

        List<FreeRoomCountVO> freeRoomCountVOS = roomService.getFreeRoomCountByDatesForHotel(1L, fromDate, toDate);

        assertNotNull(freeRoomCountVOS);
        assertEquals(7, freeRoomCountVOS.size()); // One for initial type counts and 6 for each day in the range
        FreeRoomCountVO initialTypeCount = freeRoomCountVOS.get(0);
        assertNull(initialTypeCount.date());
        assertEquals(1, initialTypeCount.typesCount().size());
        assertEquals(2, initialTypeCount.typesCount().get(0).getCount()); // Initial count of rooms of the type

        for (int i = 1; i < freeRoomCountVOS.size(); i++) {
            FreeRoomCountVO freeRoomCountVO = freeRoomCountVOS.get(i);
            assertNotNull(freeRoomCountVO.date());
            assertEquals(1, freeRoomCountVO.typesCount().size());
            assertEquals(2, freeRoomCountVO.typesCount().get(0).getCount()); // All rooms are free
        }
    }

    @Test
    void testGetFreeRoomCountByDatesForHotelWithTakenRooms() {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(5);

        Room takenRoom = new Room("Room 103", RoomStatus.FREE, List.of(type), hotel);
        takenRooms.add(takenRoom);

        when(roomRepository.findAllByHotel_Id(1L)).thenReturn(rooms);
        when(roomRepository.findAllFinishedByHotelForDate(anyLong(), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(roomRepository.findAllTakenByHotelForDate(anyLong(), any(LocalDate.class))).thenReturn(takenRooms);

        List<FreeRoomCountVO> freeRoomCountVOS = roomService.getFreeRoomCountByDatesForHotel(1L, fromDate, toDate);

        assertNotNull(freeRoomCountVOS);
        assertEquals(7, freeRoomCountVOS.size()); // One for initial type counts and 6 for each day in the range
        FreeRoomCountVO initialTypeCount = freeRoomCountVOS.get(0);
        assertNull(initialTypeCount.date());
        assertEquals(1, initialTypeCount.typesCount().size());
        assertEquals(2, initialTypeCount.typesCount().get(0).getCount()); // Initial count of rooms of the type

        for (int i = 1; i < freeRoomCountVOS.size(); i++) {
            FreeRoomCountVO freeRoomCountVO = freeRoomCountVOS.get(i);
            assertNotNull(freeRoomCountVO.date());
            assertEquals(1, freeRoomCountVO.typesCount().size());
            assertEquals(1, freeRoomCountVO.typesCount().get(0).getCount()); // One room is taken
        }
    }
}
