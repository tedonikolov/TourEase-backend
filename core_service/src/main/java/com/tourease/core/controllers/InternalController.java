package com.tourease.core.controllers;

import com.tourease.core.models.dto.ReservationStatusDTO;
import com.tourease.core.models.dto.ReservationUpdateVO;
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

    @Operation(description = "Check if reservation exist",
            summary = "Check if reservation exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @GetMapping("/reservation/{reservationNumber}")
    public ResponseEntity<Boolean> checkReservation(@PathVariable Long reservationNumber) {
        return ResponseEntity.ok(reservationService.checkReservation(reservationNumber));
    }

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @PutMapping("/reservation/update")
    public ResponseEntity<Void> updateReservation(@RequestBody ReservationUpdateVO reservationUpdateVO) {
        reservationService.updateReservation(reservationUpdateVO);
        return ResponseEntity.ok().build();
    }
}
