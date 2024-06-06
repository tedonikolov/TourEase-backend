package com.tourease.hotel;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Meal;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.MealType;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.services.communication.BookingApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

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
            createMeals();
        }
    }

    private void createMeals(){
        List<Hotel> hotels = hotelRepository.getAll();
        for (Hotel hotel : hotels) {
            Set<Meal> meals = new HashSet<>();

            Random random = new Random();
            BigDecimal price = BigDecimal.valueOf(random.nextInt(30) + 10);

            meals.add(new Meal(MealType.BREAKFAST, price, Currency.EUR, hotel));
            meals.add(new Meal(MealType.HALFBOARD, price.add(new BigDecimal(10)), Currency.EUR, hotel));
            meals.add(new Meal(MealType.FULLBOARD, price.add(new BigDecimal(15)), Currency.EUR, hotel));
            meals.add(new Meal(MealType.ALLINCLUSIVE, price.add(new BigDecimal(20)), Currency.EUR, hotel));
            hotel.setMeals(meals);
            hotelRepository.save(hotel);
        }
    }
}
