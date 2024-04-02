package com.tourease.hotel;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Room;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.services.communication.BookingApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            createRooms();
        }
    }

    private void createRooms(){
        List<Hotel> hotels = hotelRepository.getAll();
        for (Hotel hotel : hotels) {
            Set<Room> rooms = new HashSet<>();
            int j = 1;
            for (Type type : hotel.getTypes()) {
                for (int i = 0; i < 6; i++) {
                    int number = (j*100+i);
                    List<Type> types = new ArrayList<>();
                    types.add(type);

                    rooms.add(new Room( Integer.toString(number), types, hotel));
                }
                j++;
            }
            hotel.setRooms(rooms);
            hotelRepository.save(hotel);
        }
    }
}
