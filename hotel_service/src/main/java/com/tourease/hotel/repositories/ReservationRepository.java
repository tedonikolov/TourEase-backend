package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT r FROM Reservation r
            WHERE r.room.id = :id
            AND ((r.checkIn BETWEEN cast(:fromDate as date) AND cast(:toMinusDay as date)
            OR r.checkOut BETWEEN cast(:fromPlusDay as date) AND cast(:toDate as date))
            OR (r.checkIn < cast(:fromDate as date) AND r.checkOut > cast(:toDate as date)))
            AND (r.status = 'CONFIRMED' OR r.status = 'ACCOMMODATED')
            """)
    List<Reservation> isRoomTaken(Long id, OffsetDateTime fromDate, OffsetDateTime fromPlusDay, OffsetDateTime toDate, OffsetDateTime toMinusDay);
}