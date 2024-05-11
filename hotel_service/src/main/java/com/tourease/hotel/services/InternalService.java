package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.PaymentCreateVO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.response.DataSet;
import com.tourease.hotel.models.dto.response.FreeRoomCountVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class InternalService {
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final TypeService typeService;
    private final MealService mealService;

    public DataSet getDataSet() {
        List<Hotel> hotels = hotelRepository.findAll();


        return new DataSet(
                hotels.stream().map(hotel -> hotel.getLocation().getCountry()).distinct().toList(),
                hotels.stream().map(hotel -> hotel.getLocation().getCity()).distinct().toList(),
                hotels.stream().map(hotel -> hotel.getLocation().getAddress()).distinct().toList(),
                hotels.stream().map(Hotel::getName).distinct().toList()
        );
    }

    public List<LocalDate> getNotAvailableDates(Long hotelId, Long typeId, LocalDate fromDate, LocalDate toDate) {
        List<LocalDate> notAvailableDates = new ArrayList<>();

        List<FreeRoomCountVO> freeRoomCount = roomService.getFreeRoomCountByDatesForHotel(hotelId, fromDate, toDate);

        for (FreeRoomCountVO freeRoom : freeRoomCount) {
            if (freeRoom.typesCount().stream().anyMatch(typeCount -> typeCount.getId().equals(typeId) && typeCount.getCount() == 0)) {
                notAvailableDates.add(freeRoom.date());
            }
        }

        return notAvailableDates;
    }

}
