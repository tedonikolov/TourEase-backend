package com.tourease.logger.controllers;

import com.tourease.logger.models.dto.ChronologyFilter;
import com.tourease.logger.models.dto.MessageLog;
import com.tourease.logger.services.ChronologyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ChronologyService chronologyService;

    @PostMapping("/getAll")
    public ResponseEntity<List<MessageLog>> getAll(@RequestBody ChronologyFilter chronologyFilter) {
        return ResponseEntity.ok(chronologyService.gerChronology(chronologyFilter));
    }

    @GetMapping("/getTypes")
    public ResponseEntity<Set<String>> getTypes(){
        return ResponseEntity.ok(chronologyService.getTypes());
    }
}
