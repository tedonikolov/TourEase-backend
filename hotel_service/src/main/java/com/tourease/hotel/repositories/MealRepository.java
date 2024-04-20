package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Meal;
import com.tourease.hotel.models.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query("SELECT m FROM Meal m WHERE m.type = :type AND m.hotel.id = :hotelId")
    Optional<Meal> findByTypeAndHotel_Id(MealType type, Long hotelId);
}