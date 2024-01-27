package com.tourease.configuration.repositories;

import com.tourease.configuration.models.entities.Configuration;
import com.tourease.configuration.models.enums.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Configuration findByName(Field name);
}
