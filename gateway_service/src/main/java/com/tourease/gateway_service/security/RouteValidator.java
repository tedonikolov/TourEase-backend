package com.tourease.gateway_service.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public final String[] openUrlMatchers = {
            "/login",
            "/user-service/user/registration",
            "/user-service/user/login",
            "/user-service/user/activateUser",
            "/user-service/user/activateUser/*",
            "/user-service/user/sendActivateEmail/",
            "/user-service/user/sendActivateEmail/*",
    };
    public final String[] allowedUrlMatchers = {
            "/user-service/user/getLoggedUser/*",
    };

    public final String[] regularAllowedUrlMatchers = {
            "/user-service/regular/**",
    };

    public final String[] hotelAllowedUrlMatchers = {
            "/user-service/hotel/*",
    };

    public final String[] transportAllowedUrlMatchers = {
            "/user-service/transport/*",
    };

    public final String[] adminAllowedUrlMatchers = {
            "/user-service/admin/**",
            "/logger-service/admin/**"
    };

    public Predicate<ServerHttpRequest> isAllowed =
            request -> Arrays.stream(allowedUrlMatchers)
                    .anyMatch(uri -> request.getURI().getPath().matches(uri));

    public Predicate<ServerHttpRequest> isSecured =
            request -> Arrays.stream(openUrlMatchers).toList()
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
