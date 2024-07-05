package com.tourease.core.repositories;

import com.tourease.core.models.entities.Reservation;
import com.tourease.core.models.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where r.reservationNumber = ?1")
    Optional<Reservation> findByReservationNumber(Long reservationNumber);

    @Query("select r from Reservation r where r.userId = :userId " +
            "and (:reservationNumber is null or cast(r.reservationNumber as string) LIKE concat('%',cast(:reservationNumber as string),'%')) " +
            "and (:status is null or r.status = :status) " +
            "and (cast(:creationDate as date) is null or r.creationDate BETWEEN :creationDate AND :creationDateEnd) " +
            "and (cast(:checkIn as date) is null or r.checkIn = :checkIn) " +
            "ORDER BY r.creationDate DESC")
    Page<Reservation> findByUserId(Long userId, Long reservationNumber, OffsetDateTime creationDate, OffsetDateTime creationDateEnd, LocalDate checkIn, ReservationStatus status, Pageable pageable);
}
