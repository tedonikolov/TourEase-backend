package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT r FROM Reservation r
            WHERE r.room.id = :id
            AND (r.checkIn >= cast(:fromDate as date) AND r.checkIn < cast(:toDate as date)
                OR r.checkOut > cast(:fromDate as date) AND r.checkOut <= cast(:toDate as date)
                OR r.checkIn <= cast(:fromDate as date) AND r.checkOut >= cast(:toDate as date))
            AND (r.status = 'CONFIRMED' OR r.status = 'ACCOMMODATED' OR r.status = 'PENDING')
            """)
    List<Reservation> isRoomTaken(Long id, LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.type.hotel.id = :hotelId
            AND ((cast(:date as date) BETWEEN r.checkIn AND r.checkOut
            AND (r.status = 'CONFIRMED' OR r.status = 'ACCOMMODATED' OR r.status = 'ENDING'))
            OR (cast(:date as date) >= r.checkIn AND (cast(:date as date) < r.checkOut
            AND r.status = 'FINISHED')))
            """)
    List<Reservation> findAllByRoomHotelIdAndDate(Long hotelId, LocalDate date);

    @Query("""
            SELECT r FROM Reservation r
            LEFT JOIN FETCH r.worker
            LEFT JOIN FETCH r.room
            WHERE r.type.hotel.id = :hotelId
            AND r.checkIn = :date
            AND r.status = 'CONFIRMED'
            """)
    List<Reservation> findAllConfirmedByHotelIdAndDate(Long hotelId, LocalDate date);

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
            AND r.checkIn = :date
            AND (r.status = 'CANCELLED' OR r.status = 'NO_SHOW')
            """)
    List<Reservation> findAllCanceledByHotelId(Long hotelId, LocalDate date);

    @Query("select r from Reservation r where r.reservationNumber = ?1")
    Reservation findByReservationNumber(Long reservationNumber);
}