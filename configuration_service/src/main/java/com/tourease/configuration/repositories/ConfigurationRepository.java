package com.tourease.configuration.repositories;

import com.tourease.configuration.models.collections.Configuration;
import com.tourease.configuration.models.enums.Field;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, Long> {
    Configuration findByName(Field name);
}
