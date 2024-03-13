package com.tourease.configuration.controller;

import com.tourease.configuration.models.dto.request.UpdateConfigurationDTO;
import com.tourease.configuration.models.dto.response.AllConfigurations;
import com.tourease.configuration.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Update configuration values.",
            description = "Used to update configuration values that are used in different scenarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, updated configuration.")})
    @PostMapping("/save")
    public ResponseEntity<Void> saveAllConfigurations(@RequestBody List<UpdateConfigurationDTO> configurationSave) {
        adminService.save(configurationSave);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Gets all configurations.",
            description = "Used to get all configurations that are supported by system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, returns all configurations.")})
    @GetMapping("/getAllConfigurations")
    public ResponseEntity<AllConfigurations> getAllConfigurations() {
        return ResponseEntity.ok(adminService.getAllConfigurations());
    }
}
