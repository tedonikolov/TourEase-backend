package com.tourease.hotel.services;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@EnableScheduling
public class ScheduledService {
    private final ReservationService reservationService;

    @Scheduled(cron = "0 10 0 * * *")
    public void changeReservationStatusToEnding() {
        reservationService.changeReservationStatusToEnding();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void changeReservationStatusToNoShow() {
        reservationService.changeReservationStatusToNoShow();
    }
}
