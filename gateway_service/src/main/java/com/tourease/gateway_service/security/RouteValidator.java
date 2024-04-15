package com.tourease.gateway_service.security;

import org.springframework.stereotype.Component;


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
            "/hotel-service/hotel/image",
            "/hotel-service/hotel/image/*",
            "/hotel-service/hotel/room/getFreeRoomCountByDatesForHotel",
    };
    public final String[] allowedUrlMatchers = {
            "/user-service/user/getLoggedUser/*",
            "/configuration-service/currency/getCurrencyRates",
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
            "/hotel-service/reservation/worker/**",
            "/hotel-service/hotel/room/getRoomById",
            "/hotel-service/hotel/room/getReservationForRoom",
            "/hotel-service/hotel/room/changeStatus",
            "/hotel-service/hotel/room/changeStatus/*",
            "/hotel-service/hotel/room/getTakenDaysForRoom",
            "/hotel-service/hotel/customer/getCustomerByPassportId",
            "/hotel-service/hotel/customer/getCustomerByPassportId/*",
            "/hotel-service/hotel/types/getTypesByRoomId",
            "/hotel-service/hotel/payment/worker/**",
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
}
