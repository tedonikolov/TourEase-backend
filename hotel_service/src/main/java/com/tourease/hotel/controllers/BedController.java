package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.BedVO;
import com.tourease.hotel.services.BedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/bed")
@AllArgsConstructor
public class BedController {
    private final BedService bedService;

    @Operation(description = "Create/update bed",
            summary = "Create/update bed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved bed")
    })
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody BedVO bedVO) {
        bedService.save(bedVO);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete facility by id",
            summary = "Deletes facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted facility")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bedService.delete(id);
        return ResponseEntity.ok().build();
    }
}
