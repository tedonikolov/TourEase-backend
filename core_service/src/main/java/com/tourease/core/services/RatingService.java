package com.tourease.core.services;

import com.tourease.core.models.dto.RatingVO;
import com.tourease.core.models.entities.Rating;
import com.tourease.core.repositories.RatingRepository;
import com.tourease.core.services.communication.HotelServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final HotelServiceClient hotelServiceClient;

    public void rate(Long reservationId, RatingVO rating) {
        ratingRepository.save(Rating.builder()
                .id(reservationId)
                .hotelId(rating.hotelId())
                .rate(BigDecimal.valueOf(rating.rating()))
                .comment(rating.comment())
                .build());

        hotelServiceClient.rateHotel(rating);
    }
}
