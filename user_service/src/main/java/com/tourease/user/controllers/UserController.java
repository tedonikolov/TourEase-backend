package com.tourease.user.controllers;

import com.tourease.user.models.dto.request.UserRegistration;
import com.tourease.user.models.dto.response.LoginResponse;
import com.tourease.user.models.dto.response.UserVO;
import com.tourease.user.services.EmailSenderService;
import com.tourease.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Operation(hidden = true)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authorizeLogin(@RequestParam String username, String password) {
        return ResponseEntity.ok(userService.authorize(username, password));
    }

    @Operation(description = "Creates a new trouble ticket. Can be accessed by: Distributor, Installer")
    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@RequestBody @Valid UserRegistration userRegistration) {
        userService.register(userRegistration);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendActivateEmail/{email}")
    public ResponseEntity<Void> sendActivateEmail(@PathVariable String email){
        emailSenderService.sendActivationMail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activateUser/{email}")
    public ResponseEntity<Void> activateUser(@PathVariable String email) {
        userService.activateUser(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getLoggedUser/{email}")
    public ResponseEntity<UserVO> getLoggedUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.getLoggedUser(email));
    }
}
