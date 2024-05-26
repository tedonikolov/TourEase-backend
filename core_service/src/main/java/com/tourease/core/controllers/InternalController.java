package com.tourease.core.controllers;

import com.tourease.core.models.dto.ReservationStatusDTO;
import com.tourease.core.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@AllArgsConstructor
public class InternalController {
    private final ReservationService reservationService;

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @PutMapping("/reservation/changeStatus")
    public ResponseEntity<Void> changeReservationStatusToCancelled(@RequestBody ReservationStatusDTO reservationStatus) {
        reservationService.changeReservationStatus(reservationStatus);
        return ResponseEntity.ok().build();
    }
}
