package com.tourease.configuration.controller;

import com.tourease.configuration.models.dto.request.UpdateConfigurationDTO;
import com.tourease.configuration.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/save")
    public ResponseEntity<Void> saveAllConfigurations(@RequestBody List<UpdateConfigurationDTO> configurationSave) {
        adminService.save(configurationSave);
        return ResponseEntity.ok().build();
    }
}
