package com.tourease.user.services;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@EnableScheduling
public class ScheduledService {
    private final PassportService passportService;

    @Scheduled(cron = "0 2 0 * * *")
    public void passportDateCheck(){
        passportService.checkExpiredPassports();
    }
}
