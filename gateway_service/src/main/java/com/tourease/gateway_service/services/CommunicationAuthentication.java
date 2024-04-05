package com.tourease.gateway_service.services;

import com.tourease.gateway_service.models.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CommunicationAuthentication {
    private WebClient.Builder webClientBuilder;

    public Mono<LoginResponse> getLoginResponse(String username, String password) {
        return webClientBuilder.build()
                .post()
                .uri("http://user-service/user/login")
                .body(BodyInserters.fromFormData("username", username).with("password", password))
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .filter(Objects::nonNull)
                .onErrorResume(throwable -> Mono.empty());
    }

    public Mono<String> generateToken(String sessionId) {
        return webClientBuilder.build()
                .post()
                .uri("http://authentication-service/internal/generateToken")
                .body(Mono.just(sessionId), String.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> retrieveSessionId(String token) {
        return webClientBuilder.build()
                .post()
                .uri("http://authentication-service/internal/retrieveSessionId")
                .body(Mono.just(token), String.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
