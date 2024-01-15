package com.tourease.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.server.session.InMemoryWebSessionStore;

@Configuration
public class AppConfig {

    @Bean
    public InMemoryWebSessionStore sessionStore() {
        return new InMemoryWebSessionStore();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
