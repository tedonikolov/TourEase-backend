package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.RoomVO;
import com.tourease.hotel.models.dto.response.FreeRoomCountVO;
import com.tourease.hotel.models.dto.response.RoomReservationVO;
import com.tourease.hotel.models.entities.Room;
import com.tourease.hotel.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

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

    @Operation(description = "Get room by id",
            summary = "Get room by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get room")
    })
    @GetMapping("/getRoomById")
    public ResponseEntity<Room> findById(@RequestHeader Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @Operation(description = "Get room reservation for date",
            summary = "Get room reservation for date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get reservation")
    })
    @GetMapping("/getReservationForRoom")
    public ResponseEntity<RoomReservationVO> getReservationForRoom(@RequestHeader Long id, @RequestParam LocalDate date) {
        return ResponseEntity.ok(roomService.getReservationForRoom(id, date));
    }

    @Operation(description = "Get room reservations",
            summary = "Get room reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get reservations")
    })
    @GetMapping("/getTakenDaysForRoom")
    public ResponseEntity<List<OffsetDateTime>> getTakenDaysForRoom(@RequestHeader Long id) {
        return ResponseEntity.ok(roomService.getTakenDaysForRoom(id));
    }

    @Operation(description = "Get room count between dates for hotel",
            summary = "Get room count between dates for hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful returns reservations")
    })
    @GetMapping("/getFreeRoomCountByDatesForHotel")
    public ResponseEntity<List<FreeRoomCountVO>> getFreeRoomCountByDatesForHotel(@RequestHeader Long hotelId, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        return ResponseEntity.ok(roomService.getFreeRoomCountByDatesForHotel(hotelId, fromDate, toDate));
    }

    @Operation(description = "Get all free rooms by date for hotel",
            summary = "Get room count between dates for hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful returns reservations")
    })
    @GetMapping("/getFreeRoomsForDateByTypeId")
    public ResponseEntity<List<Room>> getFreeRoomsForDateByTypeId(@RequestHeader Long hotelId, @RequestHeader Long typeId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(roomService.getFreeRoomsForDateByTypeId(hotelId, typeId, date));
    }
}
