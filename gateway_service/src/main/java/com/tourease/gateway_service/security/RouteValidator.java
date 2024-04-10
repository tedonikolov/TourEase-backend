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
            "/user-service/user/sendPasswordChangeEmail/",
            "/user-service/user/sendPasswordChangeEmail/*",
            "/user-service/user/changePassword",
            "/configuration-service/country/all",
            "/hotel-service/hotel/image",
            "/hotel-service/hotel/image/*"
    };
    public final String[] allowedUrlMatchers = {
            "/user-service/user/getLoggedUser/*",
    };

    public final String[] regularAllowedUrlMatchers = {
            "/user-service/regular/**",
    };

    public final String[] hotelAllowedUrlMatchers = {
            "/hotel-service/owner/**",
            "/hotel-service/hotel/**",
    };

    public final String[] workingEndpointsAllowedUrlMatchers = {
            "/hotel-service/worker/**",
            "/hotel-service/reservation/worker/getAllReservationsViewByHotel",
            "/hotel-service/hotel/room/changeStatus",
            "/hotel-service/hotel/room/changeStatus/*",
            "/hotel-service/hotel/customer/getCustomerByPassportId",
            "/hotel-service/hotel/customer/getCustomerByPassportId/*",
    };

    public final String[] adminAllowedUrlMatchers = {
            "/user-service/admin/**",
            "/user-service/swagger",
            "/hotel-service/swagger",
            "/configuration-service/swagger",
            "/configuration-service/admin/**",
            "/logger-service/swagger",
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
