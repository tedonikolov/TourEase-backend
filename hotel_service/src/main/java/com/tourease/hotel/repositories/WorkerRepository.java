package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query("select w from Worker w where w.email = ?1")
    Worker findByEmail(String email);
}