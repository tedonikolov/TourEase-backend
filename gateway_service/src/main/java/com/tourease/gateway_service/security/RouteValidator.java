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
            "/hotel-service/image/getImage",
            "/hotel-service/image/getImage/*",
            "/hotel-service/image/getForHotel",
            "/hotel-service/image/getForHotel/*",
            "/core-service/search/listing"
    };
    public final String[] allowedUrlMatchers = {
            "/user-service/user/getLoggedUser/*",
            "/configuration-service/currency/getCurrencyRates",
    };

    public final String[] regularAllowedUrlMatchers = {
            "/user-service/regular/**",
            "/core-service/search/getNotAvailableDates",
            "/core-service/reservation/",
            "/core-service/reservation/*",
    };

    public final String[] ownerAllowedUrlMatchers = {
            "/hotel-service/owner/**",
    };

    public final String[] ownerAndManagerAllowedUrlMatchers = {
            "/hotel-service/owner/**",
            "/hotel-service/hotel/saveHotel",
            "/hotel-service/image/hotel",
            "/hotel-service/image/hotel/*",
            "/hotel-service/hotel/facility",
            "/hotel-service/hotel/facility/*",
            "/hotel-service/hotel/meal",
            "/hotel-service/hotel/meal/*",
            "/hotel-service/hotel/bed",
            "/hotel-service/hotel/bed/*",
            "/hotel-service/hotel/type",
            "/hotel-service/hotel/type/*",
            "/hotel-service/hotel/room",
            "/hotel-service/hotel/room/*",
            "/hotel-service/worker/type",
            "/hotel-service/worker/type/*",
            "/hotel-service/hotel/worker/save",
    };

    public final String[] managerAllowedUrlMatchers = {
            "/hotel-service/hotel/changeWorkingPeriod",
            "/hotel-service/reservation/worker/cancelReservation",
            "/hotel-service/reservation/worker/confirmReservation",
            "/hotel-service/hotel/payment/worker/deletePaymentById/",
            "/hotel-service/hotel/payment/worker/deletePaymentById/*",
    };

    public final String[] workingEndpointsAllowedUrlMatchers = {
            "/hotel-service/hotel/worker/getWorker",
            "/hotel-service/hotel/worker/getWorker/*",
            "/hotel-service/reservation/worker/createReservation",
            "/hotel-service/reservation/worker/updateReservation",
            "/hotel-service/reservation/worker/getAllReservationsViewByHotel",
            "/hotel-service/reservation/worker/getAllReservationsForDate",
            "/hotel-service/reservation/worker/addCustomer",
            "/hotel-service/reservation/worker/checkOutReservation",
            "/hotel-service/hotel/room/getRoomById",
            "/hotel-service/hotel/room/getTakenDaysForRoom",
            "/hotel-service/hotel/room/getReservationForRoom",
            "/hotel-service/hotel/room/changeStatus",
            "/hotel-service/hotel/room/changeStatus/*",
            "/hotel-service/hotel/room/getFreeRoomCountByDatesForHotel",
            "/hotel-service/hotel/room/getFreeRoomsForDateByTypeId",
            "/hotel-service/hotel/customer/getCustomerByPassportId",
            "/hotel-service/hotel/customer/getCustomerByPassportId/*",
            "/hotel-service/hotel/type/getTypesByRoomId",
            "/hotel-service/hotel/type/getTypesForPeopleCount",
            "/hotel-service/hotel/payment/worker/getAllPaymentsByCustomersForHotel",
            "/hotel-service/hotel/payment/worker/createPayment",
            "/hotel-service/hotel/payment/worker/markPaymentAsPaid",
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
