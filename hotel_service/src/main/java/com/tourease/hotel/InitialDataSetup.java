package com.tourease.hotel;

import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.services.communication.BookingApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataSetup implements CommandLineRunner {

    private final BookingApiClient client;
    private final HotelRepository hotelRepository;

    @Override
    public void run(String... args) {
        initData();
    }

    private void initData() {
        if (hotelRepository.count() == 0) {
            client.getHotels();
        }
    }
}
