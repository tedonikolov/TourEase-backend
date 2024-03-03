package com.tourease.configuration.controller;

import com.tourease.configuration.services.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/country")
@AllArgsConstructor
public class CountryController {
    private final ConfigurationService service;

    @Operation(summary = "Gets countries.",
            description = "Used to get countries that are supported by system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, returns list of countries.")})
    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllCountries(){
        return ResponseEntity.ok(service.getAllCountries());
    }
}
