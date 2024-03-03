package com.tourease.user.controllers;

import com.tourease.configuration.exception.ErrorResponse;
import com.tourease.user.models.dto.request.RegularVO;
import com.tourease.user.services.RegularService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("regular")
public class RegularController {
    private final RegularService regularService;

    @Operation(summary = "Create/Update regular info.",
            description = "Used to create or update regular user info.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, updated regular info."),
            @ApiResponse(responseCode = "400", description = "User not found.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody RegularVO regularVO){
        regularService.save(regularVO);
        return ResponseEntity.ok().build();
    }
}
