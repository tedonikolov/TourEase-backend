package com.tourease.configuration.controller;

import com.tourease.configuration.services.ConfigurationService;
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

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllCountries(){
        return ResponseEntity.ok(service.getAllCountries());
    }
}
