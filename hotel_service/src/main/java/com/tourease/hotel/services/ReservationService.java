package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.response.SchemaReservationsVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final TypeService typeService;
    private final CustomerService customerService;
    private final WorkerService workerService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createReservation(ReservationCreateDTO reservationInfo, Long userId) {
        Customer customer = customerService.findByPassportId(reservationInfo.customer().passportId());
        if (customer == null) {
            customer = customerService.createCustomer(reservationInfo.customer());
        }

        Worker worker = workerService.findById(userId);
        Set<Customer> customers = Set.of(customer);

        Room room = roomService.findById(reservationInfo.roomId());
        Type type = typeService.findById(reservationInfo.typeId());

        if (reservationRepository.isRoomTaken(room.getId(), reservationInfo.checkIn(), reservationInfo.checkIn().plusDays(1), reservationInfo.checkOut(), reservationInfo.checkOut().minusDays(1)).isEmpty()) {
            int nights = Math.toIntExact(reservationInfo.checkIn().until(reservationInfo.checkOut().withHour(23), ChronoUnit.DAYS));

            Reservation reservation = Reservation.builder()
                    .reservationNumber(reservationInfo.checkIn().getYear() * 10000000L +
                            reservationInfo.checkIn().getMonthValue() * 100000 +
                            reservationInfo.checkIn().getDayOfMonth() * 10000 +
                            room.getId() * 10 + customer.getId())
                    .customers(customers)
                    .room(room)
                    .checkIn(reservationInfo.checkIn())
                    .checkOut(reservationInfo.checkOut())
                    .nights(nights)
                    .price(BigDecimal.valueOf(type.getPrice().doubleValue() * BigDecimal.valueOf(nights).doubleValue()))
                    .currency(type.getCurrency())
                    .paid(false)
                    .worker(worker)
                    .build();

            if (worker.getWorkerType().equals(WorkerType.MANAGER)) {
                reservation.setStatus(ReservationStatus.CONFIRMED);
            } else {
                reservation.setStatus(ReservationStatus.PENDING);
            }

            reservationRepository.save(reservation);
            kafkaTemplate.send("hotel_service", worker.getEmail(), "New reservation created for hotel with name:" + worker.getHotel().getName());
        } else {
            throw new CustomException("Room is already taken", ErrorCode.AlreadyExists);
        }
    }

    public List<SchemaReservationsVO> getAllReservationsViewByHotel(Long hotelId, LocalDate date) {
        LocalDate plusDay = date.plusDays(1);
        List<Reservation> reservations = reservationRepository.findAllByRoomHotelIdAndDate(hotelId, date, plusDay);
        reservations.sort(Comparator.comparing((Reservation r) -> switch (r.getStatus()) {
            case FINISHED -> 1;
            case CONFIRMED -> 2;
            case ACCOMMODATED -> 3;
            case ENDING -> 4;
            default -> 0;
        }));
        return reservations.stream().map(SchemaReservationsVO::new).toList();
    }

    public void changeReservationStatusToEnding() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals(ReservationStatus.ACCOMMODATED) &&
                    reservation.getCheckOut().isBefore(OffsetDateTime.from(OffsetDateTime.now().plusDays(1)))) {
                reservation.setStatus(ReservationStatus.ENDING);
                reservationRepository.save(reservation);
            }
        }
    }

    public void changeReservationStatusToNoShow() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if ((reservation.getStatus().equals(ReservationStatus.PENDING) || reservation.getStatus().equals(ReservationStatus.CONFIRMED))
                    && reservation.getCheckIn().isBefore(OffsetDateTime.from(OffsetDateTime.now()))) {
                reservation.setStatus(ReservationStatus.NO_SHOW);
                reservationRepository.save(reservation);
            }
        }
    }

    public void addCustomerToReservation(Long reservationId, CustomerDTO customerDTO) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.EntityNotFound));
        Customer customer = customerService.findByPassportId(customerDTO.passportId());

        if (customer == null) {
            customer = customerService.createCustomer(customerDTO);
        } else
            customerService.updateCustomer(customer, customerDTO);

        reservation.getCustomers().add(customer);
        customer.getReservations().add(reservation);

        reservationRepository.save(reservation);
    }
}