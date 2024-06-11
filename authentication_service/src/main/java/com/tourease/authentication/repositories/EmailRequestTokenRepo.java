package com.tourease.authentication.repositories;

import com.tourease.authentication.collections.EmailRequestToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRequestTokenRepo extends MongoRepository<EmailRequestToken, String> {
    EmailRequestToken findByToken(String token);
}

