package com.tourease.gateway_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.session.HeaderWebSessionIdResolver;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Configuration
public class SessionHeaderResolver extends HeaderWebSessionIdResolver {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public SessionHeaderResolver() {
        setHeaderName(HttpHeaders.AUTHORIZATION);
    }

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public List<String> resolveSessionIds(ServerWebExchange exchange) {
        try {
            List<String> strings = exchange.getRequest().getHeaders().getOrDefault(HttpHeaders.AUTHORIZATION, Collections.emptyList());
            List<String> result = new ArrayList<>();
            for (String token : strings) {
                if (token.startsWith("Bearer")) {
                    token = token.replace("Bearer ", "");

                    Jws<Claims> user;
                    user = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token);
                    result.add(user.getBody().getSubject());
                }
            }
            return result;
        } catch (JwtException ex) {
            log.info("Invalid jwt detected while trying to access " + exchange.getRequest().getURI());
            return Collections.emptyList();
        }
    }

    @Override
    public void setSessionId(ServerWebExchange exchange, String id) {
        String jws = Jwts.builder()
                .setIssuer("TourEase Gateway")
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(2L, ChronoUnit.HOURS)))
                .setId(String.valueOf(UUID.randomUUID()))
                .signWith(key).compact();

        exchange.getResponse().getHeaders().set(getHeaderName(), jws);
    }

    public String getIdFromSession(String sessionId) {
        SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
        return info.getPrincipal().toString();
    }
}
