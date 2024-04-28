package com.tourease.core.controllers;

import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.HotelPreview;
import com.tourease.core.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @Operation(description = "Get hotels listing based on search text",
            summary = "Get hotels listing based on search text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieve information")
    })
    @GetMapping("/listing")
    public ResponseEntity<IndexVM<HotelPreview>> getHotelListing(@RequestParam String searchText, @RequestParam int page){
        return ResponseEntity.ok(searchService.listing(searchText, page));
    }
}
