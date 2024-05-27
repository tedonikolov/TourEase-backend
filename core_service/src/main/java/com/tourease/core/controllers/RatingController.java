package com.tourease.core.controllers;

import com.tourease.core.models.dto.RatingVO;
import com.tourease.core.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @Operation(description = "Give rating",
            summary = "Give rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful created reservation"),
    })
    @PostMapping("/rateHotel")
    public ResponseEntity<Void> rate(@RequestBody RatingVO rating, @RequestHeader Long reservationId) {
        ratingService.rate(reservationId, rating);
        return ResponseEntity.ok().build();
    }
}
