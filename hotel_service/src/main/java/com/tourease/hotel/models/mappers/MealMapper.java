package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.MealVo;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Meal;

public class MealMapper {
    public static Meal toEntity(MealVo mealVo, Hotel hotel) {
        return Meal.builder()
                .id(mealVo.id())
                .hotel(hotel)
                .type(mealVo.type())
                .price(mealVo.price())
                .currency(mealVo.currency())
                .build();
    }

    public static void updateEntity(Meal meal, MealVo mealVo) {
        meal.setPrice(mealVo.price());
        meal.setCurrency(mealVo.currency());
    }
}
