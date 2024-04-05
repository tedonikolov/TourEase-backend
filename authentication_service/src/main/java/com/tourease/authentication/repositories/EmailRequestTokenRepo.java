package com.tourease.authentication.repositories;

import com.tourease.authentication.entity.EmailRequestToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRequestTokenRepo extends JpaRepository<EmailRequestToken, Long> {
    @Query("select e from EmailRequestToken e where e.token = ?1")
    EmailRequestToken findByToken(String token);
}
