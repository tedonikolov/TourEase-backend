package com.tourease.authentication.services;

import com.tourease.authentication.entity.EmailRequestToken;
import com.tourease.authentication.repositories.EmailRequestTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailRequestTokenService {
private final EmailRequestTokenRepo emailRequestTokenRepo;

    public String generateToken(String email) {
        EmailRequestToken emailRequestToken = new EmailRequestToken();
        String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());

        emailRequestToken.setEmail(encodedEmail);
        emailRequestToken.setToken(UUID.randomUUID().toString());
        emailRequestToken.setExpirationDate(OffsetDateTime.now().plusMinutes(10));
        emailRequestTokenRepo.save(emailRequestToken);
        return emailRequestToken.getToken();
    }

    public String retrieveEmail(String token) {
        EmailRequestToken emailRequestToken = emailRequestTokenRepo.findByToken(token);
        if (emailRequestToken == null || emailRequestToken.getExpirationDate().isBefore(OffsetDateTime.now())) {
            return null;
        }
        //emailRequestTokenRepo.delete(emailRequestToken);
        byte[] decodedBytes = Base64.getDecoder().decode(emailRequestToken.getEmail());

        return new String(decodedBytes);
    }
}
