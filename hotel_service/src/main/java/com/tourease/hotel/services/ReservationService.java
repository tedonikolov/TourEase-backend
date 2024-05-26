package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.dto.requests.PaymentCreateVO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.requests.ReservationUpdateVO;
import com.tourease.hotel.models.dto.response.ReservationListing;
import com.tourease.hotel.models.dto.response.SchemaReservationsVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.repositories.ReservationRepository;
import com.tourease.hotel.services.communication.CoreServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final CustomerService customerService;
    private final WorkerService workerService;
    private final PaymentService paymentService;
    private final TypeService typeService;
    private final MealService mealService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CoreServiceClient coreServiceClient;

    public void createReservation(ReservationCreateDTO reservationInfo, Long userId) {
        Customer customer = !reservationInfo.customer().passportId().isEmpty() ? customerService.findByPassportId(reservationInfo.customer().passportId()) : null;
        if (customer == null) {
            customer = customerService.createCustomer(reservationInfo.customer());
        }

        Type type = typeService.findById(reservationInfo.typeId());
        Meal meal = mealService.findById(reservationInfo.mealId());
        Worker worker = workerService.findById(userId);
        Set<Customer> customers = Set.of(customer);

        Room room = roomService.findById(reservationInfo.roomId());

        if (reservationRepository.isRoomTaken(room.getId(), reservationInfo.checkIn(), reservationInfo.checkIn().plusDays(1), reservationInfo.checkOut(), reservationInfo.checkOut().minusDays(1)).isEmpty()) {

            Reservation reservation = Reservation.builder()
                    .reservationNumber(reservationInfo.checkIn().getYear() * 10000000L +
                            reservationInfo.checkIn().getMonthValue() * 100000 +
                            reservationInfo.checkIn().getDayOfMonth() * 10000 +
                            room.getId() * 10 + customer.getId())
                    .customers(customers)
                    .room(room)
                    .checkIn(reservationInfo.checkIn())
                    .checkOut(reservationInfo.checkOut())
                    .nights(reservationInfo.nights())
                    .worker(worker)
                    .type(type)
                    .meal(meal)
                    .peopleCount(reservationInfo.peopleCount())
                    .build();

            if (reservation.getCheckIn().toLocalDate().isEqual(LocalDate.now())) {
                if (reservationInfo.customer().passportId().isEmpty())
                    reservation.setStatus(ReservationStatus.CONFIRMED);
                else
                    reservation.setStatus(ReservationStatus.ACCOMMODATED);
            } else {
                if (worker.getWorkerType().equals(WorkerType.MANAGER)) {
                    reservation.setStatus(ReservationStatus.CONFIRMED);
                } else {
                    reservation.setStatus(ReservationStatus.PENDING);
                }
            }

            reservation.getCustomers().forEach(c -> c.getReservations().add(reservation));

            reservationRepository.save(reservation);

            paymentService.createPayment(new PaymentCreateVO(customer.getId(), room.getHotel().getId(), reservationInfo.price(), reservationInfo.currency(), PaidFor.RESERVATION), userId);

            kafkaTemplate.send("hotel_service", worker.getEmail(), "New reservation created for hotel with name:" + worker.getHotel().getName());
        } else {
            throw new CustomException("Room is already reserved", ErrorCode.AlreadyExists);
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

    public List<ReservationListing> getAllReservationsForDateAndStatus(Long hotelId, LocalDate date, ReservationStatus status) {
        List<Reservation> reservations = switch (status) {
            case CONFIRMED -> reservationRepository.findAllConfirmedByHotelIdAndDate(hotelId, date, date.plusDays(1));
            case PENDING -> reservationRepository.findAllPendingByHotelIdAndDate(hotelId, date, date.plusDays(1));
            case ENDING -> reservationRepository.findAllEndingByHotelId(hotelId);
            case CANCELLED -> reservationRepository.findAllCanceledByHotelId(hotelId, date, date.plusDays(1));
            default -> new ArrayList<>();
        };

        return reservations.stream().map(reservation -> {
            Customer firstCustomer = reservation.getCustomers().stream().findFirst().orElse(null);
            Payment firstPayment = firstCustomer != null ? firstCustomer.getPayments().stream().filter(payment -> !payment.isPaid()).findFirst().orElse(null) : null;

            BigDecimal totalPayment = BigDecimal.ZERO;
            Currency paymentCurrency = null;
            if (firstPayment != null) {
                totalPayment = BigDecimal.valueOf(firstCustomer.getPayments().stream().filter(payment -> !payment.isPaid()).mapToDouble(payment -> payment.getPrice().doubleValue()).sum());
                paymentCurrency = firstPayment.getCurrency();
            }

            return new ReservationListing(reservation, totalPayment, paymentCurrency, reservation.getCustomers().stream().toList());
        }).collect(Collectors.toList());
    }

    public void changeReservationStatusToEnding() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals(ReservationStatus.ACCOMMODATED) &&
                    reservation.getCheckOut().isBefore(OffsetDateTime.from(OffsetDateTime.now().plusDays(1)))) {
                changeReservationStatus(reservation.getId(), ReservationStatus.ENDING);
            }
        }
    }

    public void changeReservationStatusToNoShow() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if ((reservation.getStatus().equals(ReservationStatus.PENDING) || reservation.getStatus().equals(ReservationStatus.CONFIRMED))
                    && reservation.getCheckIn().isBefore(OffsetDateTime.from(OffsetDateTime.now()))) {

                changeReservationStatus(reservation.getId(), ReservationStatus.NO_SHOW);
            }
        }
    }

    public void addCustomerToReservation(Long reservationId, CustomerDTO customerDTO) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.EntityNotFound));
        Customer customer = customerService.findByPassportId(customerDTO.passportId());

        if (customer == null) {
            customer = customerService.findById(customerDTO.id());
            if (customer == null)
                customer = customerService.createCustomer(customerDTO);
            else
                customerService.updateCustomer(customer, customerDTO);
        } else
            customerService.updateCustomer(customer, customerDTO);

        if (reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
            if (LocalDate.now().isEqual(reservation.getCheckIn().toLocalDate())) {
                reservation.setStatus(ReservationStatus.ACCOMMODATED);
            }

            Set<Customer> reservationCustomers = reservation.getCustomers();
            reservation.setCustomers(new HashSet<>(Collections.singletonList(customer)));

            Customer finalCustomer = customer;
            reservationCustomers.forEach(c -> {
                        c.getPayments().forEach(payment -> {
                                    if (payment.getPaidFor().equals(PaidFor.RESERVATION) && !payment.isPaid() && payment.getHotel() == reservation.getRoom().getHotel()) {
                                        payment.setCustomer(finalCustomer);
                                    }
                                }
                        );
                        c.setPayments(new HashSet<>());
                    }
            );

            customerService.deleteAll(reservationCustomers);
        } else if (reservation.getStatus().equals(ReservationStatus.ACCOMMODATED)) {
            reservation.getCustomers().add(customer);
        }

        customer.getReservations().add(reservation);
        reservationRepository.save(reservation);
    }

    public void changeReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.EntityNotFound));
        reservation.setStatus(status);

        if(status.equals(ReservationStatus.CANCELLED))
            paymentService.removeReservationPayment(reservation.getCustomers().stream().map(Customer::getId).toList(), reservation.getType().getHotel().getId());

        reservationRepository.save(reservation);

        coreServiceClient.checkConnection();
        coreServiceClient.changeStatus(reservation.getReservationNumber(), status);
    }

    public void updateReservation(ReservationUpdateVO reservationInfo, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationInfo.id()).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.EntityNotFound));
        Room room = reservationInfo.roomId() != null ? roomService.findByIdAndType(reservationInfo.roomId(), reservationInfo.typeId(), reservationInfo.peopleCount()) : reservation.getRoom();
        Type type = typeService.findById(reservationInfo.typeId());
        Meal meal = mealService.findById(reservationInfo.mealId());

        reservation.setCheckIn(reservationInfo.checkIn());
        reservation.setCheckOut(reservationInfo.checkOut());
        reservation.setNights(reservationInfo.nights());
        reservation.setPeopleCount(reservationInfo.peopleCount());
        reservation.setMeal(meal);
        reservation.setType(type);

        if (room != null && reservationRepository.isRoomTaken(room.getId(), reservationInfo.checkIn(), reservationInfo.checkIn().plusDays(1), reservationInfo.checkOut(), reservationInfo.checkOut().minusDays(1)).stream().filter(reservation1 -> !reservation1.getId().equals(reservationInfo.id())).toList().isEmpty()) {
            reservation.setRoom(room);
        } else {
            throw new CustomException("Room is already reserved", ErrorCode.AlreadyExists);
        }

        paymentService.removeReservationPayment(reservationInfo.customers(), reservation.getRoom().getHotel().getId());
        paymentService.createPayment(new PaymentCreateVO(reservation.getCustomers().stream().findFirst().get().getId(), reservation.getRoom().getHotel().getId(), reservationInfo.price(), reservationInfo.currency(), PaidFor.RESERVATION), userId);

        reservationRepository.save(reservation);
    }
}