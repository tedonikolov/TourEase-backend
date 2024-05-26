package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.PaymentCreateVO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.response.DataSet;
import com.tourease.hotel.models.dto.response.FreeRoomCountVO;
import com.tourease.hotel.models.dto.response.HotelVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Long createReservation(ReservationCreateDTO reservationInfo) {

        Customer customer = customerService.findByPassportId(reservationInfo.customer().passportId());

        if (customer == null) {
            customer = customerService.createCustomer(reservationInfo.customer());
        }

        Type type = typeService.findById(reservationInfo.typeId());
        Meal meal = mealService.findById(reservationInfo.mealId());
        Set<Customer> customers = Set.of(customer);

        Reservation reservation = Reservation.builder()
                .reservationNumber(LocalDateTime.now().getYear() * 10000000L +
                        LocalDateTime.now().getMonthValue() * 100000 +
                        LocalDateTime.now().getDayOfMonth() * 10000 +
                        LocalDateTime.now().getHour() * 1000 + LocalDateTime.now().getMinute() * 100 + LocalDateTime.now().getSecond())
                .customers(customers)
                .checkIn(reservationInfo.checkIn())
                .checkOut(reservationInfo.checkOut())
                .nights(reservationInfo.nights())
                .type(type)
                .meal(meal)
                .peopleCount(reservationInfo.peopleCount())
                .status(ReservationStatus.PENDING)
                .build();

            reservation.getCustomers().forEach(c -> c.getReservations().add(reservation));

            reservationRepository.save(reservation);

            paymentService.createPayment(new PaymentCreateVO(customer.getId(), reservationInfo.hotelId(), reservationInfo.price(), reservationInfo.currency(), PaidFor.RESERVATION), null);
            return reservation.getReservationNumber();
    }

    public HotelVO getHotelByReservationNumber(Long reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);

        if (reservation == null) {
            return null;
        }

        return new HotelVO(reservation.getType().getHotel().getName(), reservation.getType().getHotel().getStars(),
                reservation.getType().getHotel().getLocation().getCountry(), reservation.getType().getHotel().getLocation().getCity(), reservation.getType().getHotel().getLocation().getAddress());
    }

    public void changeReservationStatus(Long reservationNumber, ReservationStatus status) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);

        if (reservation != null) {
            reservation.setStatus(status);
            reservationRepository.save(reservation);
        }
    }
}
