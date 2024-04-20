package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    @Query("select t from Type t inner join t.rooms rooms where rooms.id = ?1")
    List<Type> findByRooms_Id(Long id);

    @Query("select t from Type t where t.hotel.id = ?1")
    List<Type> findByHotel_Id(Long id);
}