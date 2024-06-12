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

    @Query("SELECT r FROM Room r JOIN r.types t WHERE t.id = :typeId")
    List<Room> findAllByType(Long typeId);

    @Query("""
            SELECT r FROM Room r
            jOIN r.reservations res
            WHERE r.hotel.id = :hotelId
            AND cast(:date as date) BETWEEN res.checkIn AND res.checkOut
            AND (res.status = 'CONFIRMED' OR res.status = 'ACCOMMODATED' OR res.status = 'FINISHED')
            """)
    List<Room> findAllFinishedByHotelForDate(Long hotelId, LocalDate date);

    @Query("""
            SELECT r FROM Room r
            jOIN r.reservations res
            WHERE r.hotel.id = :hotelId
            AND cast(:date as date) BETWEEN res.checkIn AND res.checkOut
            AND (res.status = 'CONFIRMED' OR res.status = 'ACCOMMODATED' OR res.status = 'PENDING')
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
                WHERE (res.checkIn BETWEEN cast(:fromDate as date) AND cast(:toMinusDay as date)
                OR res.checkOut BETWEEN cast(:fromPlusDay as date) AND cast(:toDate as date))
                OR (res.checkIn < cast(:fromDate as date) AND res.checkOut > cast(:toDate as date))
                AND (res.status = 'CONFIRMED' OR res.status = 'ACCOMMODATED' OR res.status = 'PENDING')
            )
            """)
    List<Room> findAllFreeByHotelBetweenDateAndType(Long hotelId, Long typeId, LocalDate fromDate, LocalDate fromPlusDay, LocalDate toDate, LocalDate toMinusDay);
}