package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.RoomVO;
import com.tourease.hotel.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/room")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @Operation(description = "Create/update room",
            summary = "Create/update room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved room")
    })
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody RoomVO roomVO) {
        roomService.save(roomVO);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete room by id",
            summary = "Deletes room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted room")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Change room status by id",
            summary = "Change room status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful changed room status")
    })
    @PutMapping("changeStatus/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id) {
        roomService.changeStatus(id);
        return ResponseEntity.ok().build();
    }
}
