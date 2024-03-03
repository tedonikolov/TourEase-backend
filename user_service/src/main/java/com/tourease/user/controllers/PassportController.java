package com.tourease.user.controllers;

import com.tourease.configuration.exception.ErrorResponse;
import com.tourease.user.models.dto.request.PassportVO;
import com.tourease.user.services.PassportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("regular/passport")
public class PassportController {
    private final PassportService passportService;

    @Operation(summary = "Create/Update passport info.",
            description = "Used to create or update regular passport info.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, updated passport info."),
            @ApiResponse(responseCode = "400", description = "User not found or not valid passport date.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody @Valid PassportVO passportVO){
        passportService.save(passportVO);
        return ResponseEntity.ok().build();
    }
}
