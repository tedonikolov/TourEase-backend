package com.tourease.gateway_service.security;

import com.tourease.gateway_service.services.CommunicationAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthManager implements ReactiveAuthenticationManager {
    @Autowired
    private CommunicationAuthentication communicationAuthentication;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        return communicationAuthentication.getLoginResponse(username, password)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
                .map(response ->
                        new UsernamePasswordAuthenticationToken(
                                response.getUsername(), authentication.getCredentials(), response.getAuthorities())
                );
    }
}
