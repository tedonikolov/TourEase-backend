package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.MealVo;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Meal;
import com.tourease.hotel.models.mappers.MealMapper;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.MealRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final HotelRepository hotelRepository;

    public void save(MealVo mealVo){
        Hotel hotel = hotelRepository.findById(mealVo.hotelId()).get();
        if(mealVo.id()==0){
            if(mealRepository.findByTypeAndHotel_Id(mealVo.type(), mealVo.hotelId()).isPresent()){
                throw new CustomException("Meal exist", ErrorCode.AlreadyExists);
            }
            Meal meal = MealMapper.toEntity(mealVo, hotel);
            mealRepository.save(meal);
        }else {
            Meal meal = mealRepository.findById(mealVo.id()).get();
            if(meal.getType()!=mealVo.type())
                throw new CustomException("Can't change meal", ErrorCode.Failed);
            MealMapper.updateEntity(meal, mealVo);
            mealRepository.save(meal);
        }
    }

    public void delete(Long id) {
        mealRepository.deleteById(id);
    }
}
