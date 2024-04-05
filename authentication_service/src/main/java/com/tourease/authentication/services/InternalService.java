package com.tourease.authentication.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class InternalService {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String sessionId){
        return Jwts.builder()
                .setIssuer("TourEase Gateway")
                .setSubject(sessionId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(2L, ChronoUnit.HOURS)))
                .setId(String.valueOf(UUID.randomUUID()))
                .signWith(key).compact();
    }

    private Jws<Claims> retrieveToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String checkToken(String token) {
        try {
            Jws<Claims> claims = retrieveToken(token);
            return claims.getBody().getSubject();
        } catch (JwtException ex){
            return null;
        }
    }
}
