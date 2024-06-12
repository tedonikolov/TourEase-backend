package com.tourease.core.controllers;

import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.HotelPreview;
import com.tourease.core.models.dto.RoomVO;
import com.tourease.core.services.SearchService;
import com.tourease.core.services.communication.HotelServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final HotelServiceClient hotelServiceClient;

    @Operation(description = "Get hotels listing based on search text",
            summary = "Get hotels listing based on search text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve information")
    })
    @GetMapping("/listing")
    public ResponseEntity<IndexVM<HotelPreview>> getHotelListing(@RequestParam String searchText, @RequestParam int page){
        return ResponseEntity.ok(searchService.listing(searchText, page));
    }

    @Operation(summary = "Retrieve not available dates.",
            description = "Returns not available dates for a given hotel and type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, created owner.")})
    @GetMapping("/getNotAvailableDates")
    public ResponseEntity<List<LocalDate>> getNotAvailableDates(@RequestParam Long hotelId, @RequestParam Long typeId, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate){
        return ResponseEntity.ok(hotelServiceClient.getNotAvailableDates(hotelId, typeId, fromDate, toDate));
    }

    @Operation(description = "Get all free rooms by date for hotel",
            summary = "Get room count between dates for hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful returns rooms")
    })
    @GetMapping("/getFreeRoomsForDateByTypeId")
    public ResponseEntity<List<RoomVO>> getFreeRoomsForDateByTypeId(@RequestHeader Long hotelId, @RequestHeader Long typeId, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        return ResponseEntity.ok(hotelServiceClient.getFreeRoomsBetweenDateByTypeId(hotelId, typeId, fromDate, toDate));
    }
}
