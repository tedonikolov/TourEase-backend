package com.tourease.configuration.controller;

import com.tourease.configuration.models.dto.response.AllConfigurations;
import com.tourease.configuration.models.dto.response.EmailInfoVO;
import com.tourease.configuration.services.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Gets all configurations.",
            description = "Used to get all configurations that are supported by system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, returns all configurations.")})
    @GetMapping("/getAllConfigurations")
    public ResponseEntity<AllConfigurations> getAllConfigurations() {
        return ResponseEntity.ok(configurationService.getAllConfigurations());
    }

    @Operation(summary = "Gets system email.",
            description = "Used to get system email which is used for email sender functionality.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, returns email info.")})
    @GetMapping("/getEmailInfo")
    public ResponseEntity<EmailInfoVO> getEmailInfo() {
        return ResponseEntity.ok(configurationService.getEmailInfo());
    }
}
