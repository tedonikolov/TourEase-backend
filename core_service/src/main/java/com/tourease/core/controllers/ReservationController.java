package com.tourease.core.controllers;

import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.ReservationCreateDTO;
import com.tourease.core.models.dto.ReservationDTO;
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

    @Operation(description = "Get user reservations",
            summary = "Get user reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful created reservation"),
    })
    @GetMapping("/getReservations")
    public ResponseEntity<IndexVM<ReservationDTO>> getReservations(@RequestHeader Long userId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(reservationService.getReservations(userId, page-1, size));
    }

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @PutMapping("/cancelReservation")
    public ResponseEntity<Void> changeReservationStatusToCancelled(@RequestHeader Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok().build();
    }
}
