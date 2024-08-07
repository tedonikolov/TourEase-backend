package com.tourease.hotel;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.hotel.models.dto.response.ReservationListing;
import com.tourease.hotel.models.dto.response.SchemaReservationsVO;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.repositories.ReservationRepository;
import com.tourease.hotel.services.*;
import com.tourease.hotel.services.communication.CoreServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private EurekaClient eurekaClient;

    @Mock
    private CoreServiceClient application;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservationsViewByHotel() {
        Long hotelId = 1L;
        LocalDate date = LocalDate.now();

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setStatus(ReservationStatus.ACCOMMODATED);
        reservation1.setRoom(new Room());
        reservation1.setCheckIn(LocalDate.now().minusDays(2));
        reservation1.setCheckOut(LocalDate.now().plusDays(2));

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setStatus(ReservationStatus.CONFIRMED);
        reservation2.setRoom(new Room());
        reservation2.setCheckIn(LocalDate.now().minusDays(1));
        reservation2.setCheckOut(LocalDate.now().plusDays(1));

        when(reservationRepository.findAllByRoomHotelIdAndDate(hotelId, date))
                .thenReturn(List.of(reservation1, reservation2));

        List<SchemaReservationsVO> result = reservationService.getAllReservationsViewByHotel(hotelId, date);

        assertEquals(2, result.size());
        assertEquals(reservation2.getId(), result.get(0).reservationId());
        assertEquals(reservation1.getId(), result.get(1).reservationId());
    }

    @Test
    public void testGetAllReservationsForDateAndStatus() {
        Long hotelId = 1L;
        LocalDate date = LocalDate.now();
        ReservationStatus status = ReservationStatus.CONFIRMED;

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationNumber(123456789L);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setRoom(new Room());
        reservation.setCheckIn(LocalDate.now().minusDays(1));
        reservation.setCheckOut(LocalDate.now().plusDays(1));
        reservation.setType(new Type());
        reservation.setMeal(new Meal());
        reservation.setPeopleCount(2);
        reservation.setNights(1);
        reservation.setCreationDate(OffsetDateTime.now());

        Payment payment = new Payment();
        payment.setHotelPrice(BigDecimal.valueOf(100));
        payment.setPrice(BigDecimal.valueOf(100));
        payment.setMealPrice(BigDecimal.valueOf(20));
        payment.setNightPrice(BigDecimal.valueOf(80));
        payment.setDiscount(BigDecimal.ZERO);
        payment.setAdvancedPayment(BigDecimal.ZERO);
        payment.setCurrency(Currency.USD);
        Hotel hotel = new Hotel();
        hotel.setCurrency(Currency.USD);
        payment.setHotel(hotel);

        when(reservationRepository.findAllConfirmedByHotelIdAndDate(hotelId, date))
                .thenReturn(List.of(reservation));
        when(paymentService.getPaymentForReservationNumber(reservation.getReservationNumber()))
                .thenReturn(payment);

        List<ReservationListing> result = reservationService.getAllReservationsForDateAndStatus(hotelId, date, status);

        assertEquals(1, result.size());
        ReservationListing listing = result.get(0);
        assertEquals(reservation.getId(), listing.id());
        assertEquals(payment.getHotelPrice(), listing.price());
    }

    @Test
    public void testChangeReservationStatusToEnding() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.ACCOMMODATED);
        reservation.setCheckOut(LocalDate.now().minusDays(1));

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(reservationRepository.findById(1l)).thenReturn(java.util.Optional.of(reservation));

        when(eurekaClient.getApplication("CORE-SERVICE")).thenReturn(new Application("CORE-SERVICE"));

        reservationService.changeReservationStatusToEnding();

        verify(reservationRepository, times(1)).save(argThat(res ->
                res.getId().equals(reservation.getId()) &&
                        res.getStatus().equals(ReservationStatus.ENDING)
        ));
    }

    @Test
    public void testChangeReservationStatusToNoShow() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setCheckIn(LocalDate.now().minusDays(1));

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(reservationRepository.findById(1l)).thenReturn(java.util.Optional.of(reservation));

        when(eurekaClient.getApplication("CORE-SERVICE")).thenReturn(new Application("CORE-SERVICE"));
        reservationService.changeReservationStatusToNoShow();

        verify(reservationRepository, times(1)).save(argThat(res ->
                res.getId().equals(reservation.getId()) &&
                        res.getStatus().equals(ReservationStatus.NO_SHOW)
        ));
    }
}
