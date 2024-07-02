package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.*;
import com.tourease.hotel.models.dto.response.ReservationListing;
import com.tourease.hotel.models.dto.response.SchemaReservationsVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.repositories.ReservationRepository;
import com.tourease.hotel.services.communication.CoreServiceClient;
import com.tourease.hotel.services.communication.EmailServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
    private final EmailServiceClient emailServiceClient;

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

        if (reservationRepository.isRoomTaken(room.getId(), reservationInfo.checkIn(), reservationInfo.checkIn().plusDays(1), reservationInfo.checkOut()).isEmpty()) {

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

            Payment payment = paymentService.createPayment(new PaymentCreateVO(customer.getId(), room.getHotel().getId(), reservationInfo.price(), reservationInfo.mealPrice(), reservationInfo.nightPrice(), reservationInfo.discount(), reservationInfo.advancedPayment(), reservationInfo.currency(), PaidFor.RESERVATION, reservation.getReservationNumber()), userId);

            if (reservation.getStatus().equals(ReservationStatus.CONFIRMED) && !reservation.getCustomers().stream().findFirst().get().getEmail().isEmpty()) {
                emailServiceClient.sendReservationConfirmation(new ReservationConfirmationVO(reservation.getCustomers().stream().findFirst().get().getEmail(), reservation.getCustomers().stream().findFirst().get().getCountry(),
                        new ReservationVO(reservation.getReservationNumber(), reservation.getCheckIn().toLocalDate(), reservation.getCheckOut().toLocalDate(), reservation.getNights(), reservation.getPeopleCount(), reservation.getType().getName(), reservation.getMeal().getType().name(), payment.getPrice(), payment.getCurrency().name(), reservation.getRoom().getHotel().getName(), reservation.getRoom().getHotel().getLocation().getCountry(), reservation.getRoom().getHotel().getLocation().getCity(), reservation.getRoom().getHotel().getLocation().getAddress(), worker.getFullName(), worker.getEmail(), worker.getPhone())));
            }

            kafkaTemplate.send("hotel_service", worker.getEmail(), "New reservation created for hotel with name:" + worker.getHotel().getName());
        } else {
            throw new CustomException("Room is already reserved", ErrorCode.AlreadyExists);
        }
    }

    public List<SchemaReservationsVO> getAllReservationsViewByHotel(Long hotelId, LocalDate date) {
        LocalDate plusDay = date.plusDays(1);
        List<Reservation> reservations = reservationRepository.findAllByRoomHotelIdAndDate(hotelId, date, plusDay);
        List<Reservation> modifiableReservations = new ArrayList<>(reservations);

        modifiableReservations.sort(Comparator.comparing((Reservation r) -> switch (r.getStatus()) {
            case FINISHED -> 1;
            case CONFIRMED -> 2;
            case ACCOMMODATED -> 3;
            case ENDING -> 4;
            default -> 0;
        }));
        return modifiableReservations.stream().map(SchemaReservationsVO::new).toList();
    }

    public List<ReservationListing> getAllReservationsForDateAndStatus(Long hotelId, LocalDate date, ReservationStatus status) {
        List<Reservation> reservations = switch (status) {
            case CONFIRMED -> reservationRepository.findAllConfirmedByHotelIdAndDate(hotelId, date, date.plusDays(1));
            case PENDING -> reservationRepository.findAllPendingByHotelId(hotelId);
            case ENDING -> reservationRepository.findAllEndingByHotelId(hotelId);
            case CANCELLED -> reservationRepository.findAllCanceledByHotelId(hotelId, date, date.plusDays(1));
            default -> new ArrayList<>();
        };

        return reservations.stream().map(reservation -> {
            Payment payment = paymentService.getPaymentForReservationNumber(reservation.getReservationNumber());

            return new ReservationListing(reservation, payment,
                    reservation.getCustomers().stream().sorted(Comparator.comparing(Customer::getId)).toList());
        }).collect(Collectors.toList());
    }

    public void changeReservationStatusToEnding() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals(ReservationStatus.ACCOMMODATED) &&
                    reservation.getCheckOut().isBefore(OffsetDateTime.from(OffsetDateTime.now().plusDays(1)))) {
                changeReservationStatus(reservation.getId(), null, ReservationStatus.ENDING);
            }
        }
    }

    public void changeReservationStatusToNoShow() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if ((reservation.getStatus().equals(ReservationStatus.PENDING) || reservation.getStatus().equals(ReservationStatus.CONFIRMED))
                    && reservation.getCheckIn().isBefore(OffsetDateTime.from(OffsetDateTime.now()))) {

                changeReservationStatus(reservation.getId(), null, ReservationStatus.NO_SHOW);
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

        } else if (reservation.getStatus().equals(ReservationStatus.ACCOMMODATED)) {
            reservation.getCustomers().add(customer);
        }

        customer.getReservations().add(reservation);
        reservationRepository.save(reservation);
    }

    public void changeReservationStatus(Long id, Long userId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.EntityNotFound));
        reservation.setStatus(status);

        if(status.equals(ReservationStatus.CANCELLED) && !reservation.getCustomers().stream().findFirst().get().getEmail().isEmpty()) {
            emailServiceClient.sendDeclinedReservation(new ReservationDeclinedVO(reservation.getReservationNumber(), reservation.getCustomers().stream().findFirst().get().getEmail(), reservation.getCustomers().stream().findFirst().get().getFullName()));
        }

        reservationRepository.save(reservation);

        if (status.equals(ReservationStatus.CONFIRMED) && !reservation.getCustomers().stream().findFirst().get().getEmail().isEmpty()) {
            Worker worker = workerService.findById(userId);
            Payment payment = paymentService.getPaymentForReservationNumber(reservation.getReservationNumber());

            emailServiceClient.sendReservationConfirmation(new ReservationConfirmationVO(reservation.getCustomers().stream().findFirst().get().getEmail(), reservation.getCustomers().stream().findFirst().get().getCountry(),
                    new ReservationVO(reservation.getReservationNumber(), reservation.getCheckIn().toLocalDate(), reservation.getCheckOut().toLocalDate(), reservation.getNights(), reservation.getPeopleCount(), reservation.getType().getName(), reservation.getMeal().getType().name(), payment.getPrice(), payment.getCurrency().name(), reservation.getRoom().getHotel().getName(), reservation.getRoom().getHotel().getLocation().getCountry(), reservation.getRoom().getHotel().getLocation().getCity(), reservation.getRoom().getHotel().getLocation().getAddress(), worker.getFullName(), worker.getEmail(), worker.getPhone())));
        }

        coreServiceClient.checkConnection();
        coreServiceClient.changeStatus(reservation.getReservationNumber(), status);
    }

    public void updateReservation(ReservationUpdateVO reservationInfo, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationInfo.id()).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.EntityNotFound));
        Room room = reservationInfo.roomId() != null ? roomService.findByIdAndType(reservationInfo.roomId(), reservationInfo.typeId(), reservationInfo.peopleCount()) : reservation.getRoom();
        Type type = typeService.findById(reservationInfo.typeId());
        Meal meal = mealService.findById(reservationInfo.mealId());

        reservation.setCheckIn(reservationInfo.checkIn());
        OffsetDateTime checkOut = reservation.getCheckOut();
        reservation.setCheckOut(reservationInfo.checkOut());
        reservation.setNights(reservationInfo.nights());
        reservation.setPeopleCount(reservationInfo.peopleCount());
        reservation.setMeal(meal);
        reservation.setType(type);

        if (room != null && reservationRepository.isRoomTaken(room.getId(), reservationInfo.checkIn(), reservationInfo.checkIn().plusDays(1), reservationInfo.checkOut()).stream().filter(reservation1 -> !reservation1.getId().equals(reservationInfo.id())).toList().isEmpty()) {
            reservation.setRoom(room);
        } else if (room != null) {
            throw new CustomException("Room is already reserved", ErrorCode.AlreadyExists);
        }

        Worker worker = workerService.findById(userId);
        if(worker.getWorkerType().equals(WorkerType.MANAGER) || checkOut!=reservationInfo.checkOut()) {
            if(reservationInfo.checkOut().toLocalDate().isEqual(LocalDate.now())) {
                reservation.setStatus(ReservationStatus.ENDING);
            } else if(reservation.getStatus() != ReservationStatus.CONFIRMED && reservation.getStatus() != ReservationStatus.PENDING
                    && (reservation.getCheckIn().toLocalDate().isBefore(LocalDate.now()) && reservation.getCheckOut().toLocalDate().isAfter(LocalDate.now()))) {
                reservation.setStatus(ReservationStatus.ACCOMMODATED);
            }

            Payment payment = paymentService.updatePayment(new PaymentCreateVO(reservation.getCustomers().stream().findFirst().get().getId(), type.getHotel().getId(), reservationInfo.price(), reservationInfo.mealPrice(), reservationInfo.nightPrice(), reservationInfo.discount(), reservationInfo.advancedPayment(), reservationInfo.currency(), PaidFor.RESERVATION, reservation.getReservationNumber()), userId);
            if(coreServiceClient.checkReservation(reservation.getReservationNumber()))
            {
                coreServiceClient.updateReservation(new ReservationClientUpdateVO(reservation.getReservationNumber(), reservationInfo.checkIn(), reservationInfo.checkOut(),
                        reservationInfo.nights(), reservationInfo.peopleCount(), payment.getPrice(), payment.getCurrency(),
                        reservation.getCustomers().stream().findFirst().get().getFullName(), reservation.getCustomers().stream().findFirst().get().getEmail()));
            }
        }

        reservationRepository.save(reservation);
    }
}