package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    @Query("select b from Bed b where b.name = ?1 and b.hotel.id = ?2")
    Optional<Bed> findByNameAndHotel_Id(String name, Long id);
}