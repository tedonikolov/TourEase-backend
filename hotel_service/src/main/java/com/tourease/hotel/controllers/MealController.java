package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.MealVo;
import com.tourease.hotel.services.MealService;
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
@RequestMapping("/hotel/meal")
@AllArgsConstructor
public class MealController {
    private final MealService mealService;

    @Operation(description = "Create/update meal",
            summary = "Create/update meal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved meal"),
            @ApiResponse(responseCode = "400", description = "Tried to save already saved meal type",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody MealVo mealVo) {
        mealService.save(mealVo);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete meal by id",
            summary = "Deletes meal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted meal")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mealService.delete(id);
        return ResponseEntity.ok().build();
    }
}
