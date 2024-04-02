package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("select c from Customer c where c.passportId = ?1")
    Optional<Customer> findByPassportId(String passportId);
}