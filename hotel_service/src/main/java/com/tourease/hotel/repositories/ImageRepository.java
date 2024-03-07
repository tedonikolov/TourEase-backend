package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional
    @Query("select i from Image i where i.hotel.id = ?1")
    List<Image> findByHotel_Id(Long id);
}