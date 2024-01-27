package com.tourease.user.controllers;

import com.tourease.user.models.dto.request.PassportVO;
import com.tourease.user.services.PassportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("regular/passport")
public class PassportController {
    private final PassportService passportService;

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody @Valid PassportVO passportVO){
        passportService.save(passportVO);
        return ResponseEntity.ok().build();
    }
}
