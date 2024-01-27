package com.tourease.user.repositories;

import com.tourease.user.models.entities.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PassportRepository extends JpaRepository<Passport, Long> {
    Passport getPassportById(Long id);

    @Query("select p from Passport p where p.expirationDate < ?1 AND p.expired=false ")
    List<Passport> findByExpirationDateBefore(LocalDate expirationDate);
}