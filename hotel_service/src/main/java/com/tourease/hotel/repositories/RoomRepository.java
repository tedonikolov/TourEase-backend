package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select r from Room r where r.name = ?1 and r.hotel.id = ?2")
    Optional<Room> findByNameAndHotel_Id(String name, Long id);

    @Query("select r from Room r where r.id = :roomId")
    Room getById(Long roomId);
}