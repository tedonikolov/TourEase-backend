package com.tourease.user.repositories;

import com.tourease.user.models.entities.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport, Long> {
    Passport getPassportById(Long id);
}