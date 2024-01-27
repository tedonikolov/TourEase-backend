package com.tourease.user.controllers;

import com.tourease.user.models.dto.request.RegularVO;
import com.tourease.user.services.RegularService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("regular")
public class RegularController {
    private final RegularService regularService;

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody RegularVO regularVO){
        regularService.save(regularVO);
        return ResponseEntity.ok().build();
    }
}
