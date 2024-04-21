package com.tourease.hotel.controllers;

import com.tourease.hotel.models.custom.IndexVM;
import com.tourease.hotel.models.dto.requests.FilterHotelListing;
import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.models.dto.requests.HotelWorkingPeriodVO;
import com.tourease.hotel.models.dto.response.HotelPreview;
import com.tourease.hotel.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
@AllArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @Operation(description = "Update/create hotel information",
            summary = "Create or update hotel information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update information"),
            @ApiResponse(responseCode = "400", description = "Hotel not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/saveHotel")
    public ResponseEntity<Void> save(@RequestBody HotelCreateVO hotelCreateVO){
        hotelService.save(hotelCreateVO);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Get hotels listing based on filter",
            summary = "Get hotels listing based on filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve information")
    })
    @GetMapping("/listing")
    public ResponseEntity<IndexVM<HotelPreview>> getHotelListing(@Parameter(name = "filter", in = ParameterIn.QUERY) FilterHotelListing filterHotelListing){

        return ResponseEntity.ok(hotelService.listing(filterHotelListing));
    }

    @Operation(description = "Change hotel working period",
            summary = "Change hotel working period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update working period"),
            @ApiResponse(responseCode = "400", description = "Hotel not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/changeWorkingPeriod")
    public ResponseEntity<Void> changeWorkingPeriod(@RequestBody HotelWorkingPeriodVO hotelWorkingPeriodVO){
        hotelService.changeWorkingPeriod(hotelWorkingPeriodVO);
        return ResponseEntity.ok().build();
    }
}
