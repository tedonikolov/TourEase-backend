package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT r FROM Reservation r
            WHERE r.room.id = :id
            AND ((r.checkIn BETWEEN cast(:fromDate as date) AND cast(:toDate as date)
            OR r.checkOut BETWEEN cast(:fromPlusDay as date) AND cast(:toDate as date))
            OR (r.checkIn < cast(:fromDate as date) AND r.checkOut > cast(:toDate as date)))
            AND (r.status = 'CONFIRMED' OR r.status = 'ACCOMMODATED' OR r.status = 'PENDING')
            """)
    List<Reservation> isRoomTaken(Long id, OffsetDateTime fromDate, OffsetDateTime fromPlusDay, OffsetDateTime toDate);

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.type.hotel.id = :hotelId
            AND ((cast(:plusDay as date) BETWEEN r.checkIn AND r.checkOut
            AND (r.status = 'CONFIRMED' OR r.status = 'ACCOMMODATED' OR r.status = 'FINISHED' OR r.status = 'ENDING'))
            OR (cast(:date as date) BETWEEN r.checkIn AND r.checkOut
            AND r.status = 'ENDING'))
            """)
    List<Reservation> findAllByRoomHotelIdAndDate(Long hotelId, LocalDate date, LocalDate plusDay);

    @Query("""
            SELECT r FROM Reservation r
            LEFT JOIN FETCH r.worker
            LEFT JOIN FETCH r.room
            WHERE r.type.hotel.id = :hotelId
            AND r.checkIn between cast(:date as date) AND cast(:plusDay as date)
            AND r.status = 'CONFIRMED'
            """)
    List<Reservation> findAllConfirmedByHotelIdAndDate(Long hotelId, LocalDate date, LocalDate plusDay);

    @Query("""
            SELECT r FROM Reservation r
            LEFT JOIN FETCH r.worker
            LEFT JOIN FETCH r.room
            WHERE r.type.hotel.id = :hotelId
            AND r.status = 'PENDING'
            """)
    List<Reservation> findAllPendingByHotelId(Long hotelId);

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.type.hotel.id = :hotelId
            AND r.status = 'ENDING'
            """)
    List<Reservation> findAllEndingByHotelId(Long hotelId);

    @Query("""
            SELECT r FROM Reservation r
            LEFT JOIN FETCH r.worker
            LEFT JOIN FETCH r.room
            WHERE r.type.hotel.id = :hotelId
            AND r.checkIn between cast(:date as date) AND cast(:plusDay as date)
            AND (r.status = 'CANCELLED' OR r.status = 'NO_SHOW')
            """)
    List<Reservation> findAllCanceledByHotelId(Long hotelId, LocalDate date, LocalDate plusDay);

    @Query("select r from Reservation r where r.reservationNumber = ?1")
    Reservation findByReservationNumber(Long reservationNumber);
}