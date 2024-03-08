package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.FacilityVO;
import com.tourease.hotel.services.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/facility")
@AllArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @Operation(description = "Create/update facility",
            summary = "Create/update facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved facility"),
            @ApiResponse(responseCode = "400", description = "Tried to save already saved facility name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody FacilityVO facilityVO) {
        facilityService.save(facilityVO);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete facility by id",
            summary = "Deletes facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted facility")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.ok().build();
    }
}
