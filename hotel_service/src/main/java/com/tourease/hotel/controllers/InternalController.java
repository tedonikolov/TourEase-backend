package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.RatingVO;
import com.tourease.hotel.models.dto.requests.ReservationCreateDTO;
import com.tourease.hotel.models.dto.response.DataSet;
import com.tourease.hotel.models.dto.response.HotelVO;
import com.tourease.hotel.models.enums.ReservationStatus;
import com.tourease.hotel.services.InternalService;
import com.tourease.hotel.services.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/internal")
@AllArgsConstructor
public class InternalController {
    private final OwnerService ownerService;
    private final InternalService internalService;

    @Operation(summary = "Creates owner.",
            description = "Creates owner by userId and with email provided form user-service, on profile activation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, created owner.")})
    @PostMapping("/createOwner")
    public ResponseEntity<Void> createOwner(@RequestParam Long id, @RequestHeader(value = "user") String email){
        ownerService.createOwner(id,email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Retrieve dataset.",
            description = "Returns hotel names, location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, created owner.")})
    @GetMapping("/getDataSet")
    public ResponseEntity<DataSet> getDataSet(){
        return ResponseEntity.ok(internalService.getDataSet());
    }

    @Operation(summary = "Retrieve not available dates.",
            description = "Returns not available dates for a given hotel and type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, created owner.")})
    @GetMapping("/getNotAvailableDates")
    public ResponseEntity<List<LocalDate>> getNotAvailableDates(@RequestParam Long hotelId, @RequestParam Long typeId, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate){
        return ResponseEntity.ok(internalService.getNotAvailableDates(hotelId, typeId, fromDate, toDate));
    }

    @Operation(description = "Create reservation",
            summary = "Create reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful created reservation")
    })
    @PostMapping("/createReservation")
    public ResponseEntity<Long> createReservation(@RequestBody ReservationCreateDTO reservationInfo) {
        return ResponseEntity.ok(internalService.createReservation(reservationInfo));
    }

    @GetMapping("/getHotelByReservationNumber/{reservationNumber}")
    public ResponseEntity<HotelVO> getHotelByReservationNumber(@PathVariable Long reservationNumber){
        return ResponseEntity.ok(internalService.getHotelByReservationNumber(reservationNumber));
    }

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @PutMapping("/cancelReservation/{reservationNumber}")
    public ResponseEntity<Void> changeReservationStatusToCancelled(@PathVariable Long reservationNumber) {
        internalService.changeReservationStatus(reservationNumber, ReservationStatus.CANCELLED);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Change status of reservation",
            summary = "Change status of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark reservation as cancel")
    })
    @PostMapping("/rate")
    public ResponseEntity<Void> rateHotel(@RequestBody RatingVO ratingVO) {
        internalService.rateHotel(ratingVO);
        return ResponseEntity.ok().build();
    }
}
