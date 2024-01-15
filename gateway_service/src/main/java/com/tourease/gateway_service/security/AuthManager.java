package com.tourease.gateway_service.security;

import com.tourease.gateway_service.models.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class AuthManager implements ReactiveAuthenticationManager {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        return getLoginResponse(username, password)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
                .map(response ->
                        new UsernamePasswordAuthenticationToken(
                                response.getUsername(), authentication.getCredentials(), response.getAuthorities())
                );
    }

    public Mono<LoginResponse> getLoginResponse(String username, String password) {
        return webClientBuilder.build()
                .post()
                .uri("http://user-service/user/login")
                .body(BodyInserters.fromFormData("username", username).with("password", password))
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .filter(Objects::nonNull)
                .onErrorResume(throwable -> {
                    log.warn("Could not connect to User Service");
                    return Mono.empty();
                });
    }

}
