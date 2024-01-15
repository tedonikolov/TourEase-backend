package com.tourease.configuration.repositories;


import com.tourease.configuration.models.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}