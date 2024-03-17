package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.WorkerVO;
import com.tourease.hotel.services.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/worker")
@AllArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @Operation(description = "Create/update worker",
            summary = "Create/update worker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved type")
    })
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody @Valid WorkerVO workerVO) {
        workerService.save(workerVO);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete type by id",
            summary = "Deletes type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted type")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> fire(@PathVariable Long id) {
        workerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete type by id",
            summary = "Deletes type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted type")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> reassign(@PathVariable Long id) {
        workerService.reassign(id);
        return ResponseEntity.ok().build();
    }
}
