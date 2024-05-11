package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.enums.FacilityEnum;
import com.tourease.hotel.models.enums.MealType;
import com.tourease.hotel.models.enums.Stars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query(value = "SELECT DISTINCT h from Hotel h " +
            "JOIN FETCH h.types t " +
            "JOIN FETCH h.beds b " +
            "JOIN Location l on h.id=l.id " +
            "LEFT JOIN Facility f on h.id=f.hotel.id " +
            "JOIN Meal m on h.id=m.hotel.id " +
            "LEFT JOIN b.types bt " +
            "LEFT JOIN t.beds tb " +
            "LEFT JOIN Rating r on h.id=r.hotel.id " +
            "WHERE (lower(l.country) LIKE concat('%',lower(:country),'%') OR :country IS NULL)" +
            "AND (lower(l.city) LIKE concat('%',lower(:city),'%') OR :city IS NULL)" +
            "AND (lower(h.name) LIKE concat('%',lower(:name),'%') OR :name IS NULL) " +
            "AND (:stars IS NULL OR h.stars=:stars) " +
            "AND (:mealType IS NULL OR m.type=:mealType) " +
            "AND (:facilities IS NULL OR f.name in :facilities) " +
            "ORDER BY h.id")
    List<Hotel> findHotelByFilter(String country, String city, String name, Stars stars, List<FacilityEnum> facilities, MealType mealType);

    @Query(value = "SELECT DISTINCT h from Hotel h " +
            "JOIN FETCH h.types t " +
            "JOIN FETCH h.beds b " +
            "ORDER BY h.id")
    List<Hotel> getAll();
}