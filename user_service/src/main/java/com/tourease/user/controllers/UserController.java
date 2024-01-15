package com.tourease.user.controllers;

import com.tourease.user.models.dto.response.LoginResponse;
import com.tourease.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(hidden = true)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authorizeLogin(@RequestParam String username, String password) {
        return ResponseEntity.ok(userService.authorize(username, password));
    }
}
