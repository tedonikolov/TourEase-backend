package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.OwnerSaveVO;
import com.tourease.hotel.models.dto.response.OwnerInfo;
import com.tourease.hotel.services.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@AllArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @Operation(description = "Get owner by email",
            summary = "Returns owner information for email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns owner information"),
            @ApiResponse(responseCode = "400", description = "Owner not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/getOwnerByEmail/{email}")
    public ResponseEntity<OwnerInfo> getOwnerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(ownerService.findOwnerByEmail(email));
    }

    @Operation(description = "Update owner information",
            summary = "Update owner information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update information"),
            @ApiResponse(responseCode = "400", description = "Invalid phone number",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/save")
    public ResponseEntity<Void> saveOwner(@RequestBody @Valid OwnerSaveVO owner) {
        ownerService.saveOwner(owner);
        return ResponseEntity.ok().build();
    }
}
