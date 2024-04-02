package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.enums.FacilityEnum;
import com.tourease.hotel.models.enums.Stars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query(value = "SELECT distinct h from Hotel h " +
            "JOIN Location l on l.id=h.id " +
            "JOIN Facility f on f.hotel.id=h.id " +
            "JOIN Bed b on b.hotel.id=h.id " +
            "JOIN Type t on t.hotel.id=h.id " +
            "LEFT JOIN Rating r on r.hotel.id=h.id " +
            "WHERE (lower(l.country) LIKE concat('%',lower(:country),'%') OR :country IS NULL)" +
            "AND ((lower(l.city) LIKE concat('%',lower(:city),'%') OR :city IS NULL)" +
            "AND (lower(l.address) LIKE concat('%',lower(:address),'%') OR :address IS NULL))" +
            "AND (lower(h.name) LIKE concat('%',lower(:name),'%') OR :name IS NULL) " +
            "AND (:stars IS NULL OR h.stars=:stars) " +
            "AND (:facilities IS NULL OR f.name in :facilities) " +
            "ORDER BY h.id")
    List<Hotel> findHotelByFilter(String country, String city, String address, String name, Stars stars, List<FacilityEnum> facilities);

    @Query(value = "SELECT DISTINCT h from Hotel h " +
            "JOIN FETCH h.types t " +
            "JOIN FETCH h.beds b " +
            "ORDER BY h.id")
    List<Hotel> getAll();
}