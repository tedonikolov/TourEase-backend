package com.tourease.core.controllers;

import com.tourease.core.models.dto.ReservationCreateDTO;
import com.tourease.core.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(description = "Create reservation",
            summary = "Create reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful created reservation"),
    })
    @PostMapping("/createReservation")
    public ResponseEntity<Void> createReservation(@RequestBody ReservationCreateDTO reservationInfo, @RequestHeader Long userId) {
        reservationService.createReservation(reservationInfo, userId);
        return ResponseEntity.ok().build();
    }
}
