package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select r from Room r where r.name = ?1 and r.hotel.id = ?2")
    Optional<Room> findByNameAndHotel_Id(String name, Long id);

    @Query("select r from Room r where r.id = :roomId")
    Room getById(Long roomId);

    @Query("select r from Room r where r.hotel.id = :hotelId")
    List<Room> findAllByHotel_Id(Long hotelId);

    @Query("""
            SELECT r FROM Room r
            jOIN r.reservations res
            WHERE r.hotel.id = :hotelId
            AND cast(:date as date) BETWEEN res.checkIn AND res.checkOut
            AND (res.status = 'CONFIRMED' OR res.status = 'ACCOMMODATED' OR res.status = 'FINISHED')
            """)
    List<Room> findAllTakenByHotelForDate(Long hotelId, LocalDate date);

    @Query("""
            SELECT r FROM Room r
            LEFT jOIN r.reservations res
            WHERE r.hotel.id = :hotelId
            AND :typeId in (SELECT t.id FROM r.types t)
            AND r NOT IN (
                SELECT r FROM Room r
                JOIN r.reservations res
                WHERE cast(:date as date) BETWEEN res.checkIn AND res.checkOut
                AND (res.status = 'CONFIRMED' OR res.status = 'ACCOMMODATED' OR res.status = 'FINISHED')
            )
            """)
    List<Room> findAllFreeByHotelForDateAndType(Long hotelId, Long typeId, LocalDate date);
}