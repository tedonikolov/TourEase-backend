package com.tourease.core;

import com.tourease.core.models.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where r.reservationNumber = ?1")
    Optional<Reservation> findByReservationNumber(Long reservationNumber);

    @Query("select r from Reservation r where r.userId = ?1 ORDER BY r.creationDate DESC")
    Page<Reservation> findByUserId(Long userId, Pageable pageable);
}
