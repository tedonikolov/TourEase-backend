package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional
    @Query("select i from Image i where i.hotel.id = ?1 ORDER BY i.hotel.id")
    List<Image> findByHotel_Id(Long id);

    @Query("select i from Image i where i.url = ?1 and i.hotel.id = ?2")
    Optional<Image> findByUrlAndHotel_Id(String url, Long id);
}