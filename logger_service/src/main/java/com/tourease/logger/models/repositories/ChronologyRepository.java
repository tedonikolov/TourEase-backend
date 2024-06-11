package com.tourease.logger.models.repositories;

import com.tourease.logger.models.collections.Chronology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChronologyRepository extends MongoRepository<Chronology, String> {

    @Query("{ 'email': { $regex: ?0, $options: 'i' }, " +
            "'log': { $regex: ?3, $options: 'i' }, " +
            "'createdOn': { $gte: ?1, $lte: ?2 } } ")
    Page<Chronology> findChronologyByFilter(String email, LocalDateTime createdAfter, LocalDateTime createdBefore, String type, Pageable pageable);
}