package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Facility;
import com.tourease.hotel.models.enums.FacilityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @Query("select f from Facility f where f.name = ?1")
    Optional<Facility> findByName(FacilityEnum name);
}