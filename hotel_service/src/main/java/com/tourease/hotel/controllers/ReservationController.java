package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.response.SchemaReservationsVO;
import com.tourease.hotel.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(description = "Create reservation",
            summary = "Create reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved room")
    })
    @PostMapping("/worker/createReservation")
    public ResponseEntity<Void> createReservation(@RequestBody ReservationCreateDTO reservationInfo, @RequestHeader Long userId) {
        reservationService.createReservation(reservationInfo, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Get all reservations for schema view",
            summary = "Get all reservations for schema view")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get all reservations")
    })
    @GetMapping("/worker/getAllReservationsViewByHotel")
    public ResponseEntity<List<SchemaReservationsVO>> getAllReservationsViewByHotel(@RequestHeader Long hotelId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(reservationService.getAllReservationsViewByHotel(hotelId, date));
    }

    @Operation(description = "Create/update customer",
            summary = "Create/update customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved room")
    })
    @PostMapping("/worker/addCustomer")
    public ResponseEntity<Void> addCustomerToReservation(@RequestHeader Long reservationId, @RequestBody CustomerDTO customerDTO) {
        reservationService.addCustomerToReservation(reservationId, customerDTO);
        return ResponseEntity.ok().build();
    }
}
