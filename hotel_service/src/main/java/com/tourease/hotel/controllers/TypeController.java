package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.TypeVO;
import com.tourease.hotel.services.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/type")
@AllArgsConstructor
public class TypeController {
    private final TypeService typeService;

    @Operation(description = "Create/update type",
            summary = "Create/update type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved type")
    })
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody TypeVO typeVO) {
        typeService.save(typeVO);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete type by id",
            summary = "Deletes type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted type")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
