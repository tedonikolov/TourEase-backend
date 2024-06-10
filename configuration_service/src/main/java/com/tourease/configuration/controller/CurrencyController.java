package com.tourease.configuration.controller;

import com.tourease.configuration.models.collections.CurrencyRate;
import com.tourease.configuration.services.CurrencyRateService;
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
@RequestMapping("/currency")
@AllArgsConstructor
public class CurrencyController {
    private final CurrencyRateService currencyRateService;

    @Operation(summary = "Gets currency rates.",
            description = "Used to get currency rates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, returns currency rates.")})
    @GetMapping("/getCurrencyRates")
    public ResponseEntity<List<CurrencyRate>> getCurrencyRates() {
        return ResponseEntity.ok(currencyRateService.getRates());
    }
}
