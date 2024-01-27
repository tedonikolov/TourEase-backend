package com.tourease.logger.controllers;

import com.tourease.logger.models.dto.ChronologyFilter;
import com.tourease.logger.models.dto.MessageLog;
import com.tourease.logger.services.ChronologyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ChronologyService chronologyService;

    @GetMapping("/getAll")
    public ResponseEntity<List<MessageLog>> getAll(@RequestBody ChronologyFilter chronologyFilter) {
        return ResponseEntity.ok(chronologyService.gerChronology(chronologyFilter));
    }
}
