package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
