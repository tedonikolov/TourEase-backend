package com.tourease.configuration.controller;

import com.tourease.configuration.models.dto.response.AllConfigurations;
import com.tourease.configuration.models.dto.response.Countries;
import com.tourease.configuration.models.dto.response.EmailInfoVO;
import com.tourease.configuration.services.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/internal")
public class InternalController {
    private final ConfigurationService configurationService;

    @GetMapping("/getAllCountries")
    public ResponseEntity<Countries> getCountries() {
        return ResponseEntity.ok(configurationService.getAllCountries());
    }

    @GetMapping("/getAllConfigurations")
    public ResponseEntity<AllConfigurations> getAllConfigurations() {
        return ResponseEntity.ok(configurationService.getAllConfigurations());
    }

    @GetMapping("/getEmailInfo")
    public ResponseEntity<EmailInfoVO> getEmailInfo() {
        return ResponseEntity.ok(configurationService.getEmailInfo());
    }
}
