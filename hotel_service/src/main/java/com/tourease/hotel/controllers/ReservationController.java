package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.requests.ReservationUpdateVO;
import com.tourease.hotel.models.dto.response.ReservationListing;
import com.tourease.hotel.models.dto.response.SchemaReservationsVO;
import com.tourease.hotel.models.enums.ReservationStatus;
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

    @Operation(description = "Update reservation",
            summary = "Update reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful check out reservation")
    })
    @PutMapping("/worker/updateReservation")
    public ResponseEntity<Void> markPayment(@RequestHeader Long workerId, @RequestBody ReservationUpdateVO reservationInfo) {
        reservationService.updateReservation(reservationInfo, workerId);
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

    @Operation(description = "Get all confirm reservations info for date",
            summary = "Get all confirm reservations info for date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get all reservations")
    })
    @GetMapping("/worker/getAllReservationsForDate")
    public ResponseEntity<List<ReservationListing>> getAllReservationsForDateAndStatus(@RequestHeader Long hotelId, @RequestParam LocalDate date, @RequestParam ReservationStatus status) {
        return ResponseEntity.ok(reservationService.getAllReservationsForDateAndStatus(hotelId, date, status));
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

    @Operation(description = "Check out reservation",
            summary = "Check out reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful check out reservation")
    })
    @PutMapping("/worker/checkOutReservation")
    public ResponseEntity<Void> changeReservationStatusToFinished(@RequestHeader Long reservationId) {
        reservationService.changeReservationStatus(reservationId, ReservationStatus.FINISHED);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @PutMapping("/worker/cancelReservation")
    public ResponseEntity<Void> changeReservationStatusToCancelled(@RequestHeader Long reservationId) {
        reservationService.changeReservationStatus(reservationId, ReservationStatus.CANCELLED);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as confirm")
    })
    @PutMapping("/worker/confirmReservation")
    public ResponseEntity<Void> changeReservationStatusToConfirmed(@RequestHeader Long reservationId) {
        reservationService.changeReservationStatus(reservationId, ReservationStatus.CONFIRMED);
        return ResponseEntity.ok().build();
    }
}
