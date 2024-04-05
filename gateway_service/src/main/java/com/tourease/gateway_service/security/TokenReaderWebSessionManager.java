package com.tourease.gateway_service.security;

import com.tourease.gateway_service.services.CommunicationAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionStore;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class TokenReaderWebSessionManager extends DefaultWebSessionManager {
    @Autowired
    private CommunicationAuthentication communicationAuthentication;
    @Autowired
    private WebSessionStore sessionStore;

    @Override
    public Mono<WebSession> getSession(ServerWebExchange exchange) {
        List<String> strings = exchange.getRequest().getHeaders().getOrDefault(HttpHeaders.AUTHORIZATION, Collections.emptyList());
        for (String token : strings) {
            if (token.startsWith("Bearer")) {
                token = token.replace("Bearer ", "");

                String finalToken = token;
                return communicationAuthentication.retrieveSessionId(finalToken)
                        .switchIfEmpty(Mono.empty())
                        .flatMap(sessionId -> sessionStore.retrieveSession(sessionId))
                        .switchIfEmpty(Mono.empty());
            }
        }
        return super.getSession(exchange); // Create a new session if no token is present
    }
}
