package com.tourease.authentication.controllers;

import com.tourease.authentication.services.EmailRequestTokenService;
import com.tourease.authentication.services.InternalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@AllArgsConstructor
public class InternalController {
    private final InternalService internalService;
    private final EmailRequestTokenService emailRequestTokenService;

    @PostMapping("/generateToken")
    public ResponseEntity<String> generateToken(@RequestBody String sessionId){
        return ResponseEntity.ok(internalService.generateToken(sessionId));
    }

    @PostMapping("/retrieveSessionId")
    public ResponseEntity<String> checkToken(@RequestBody String token){
        return ResponseEntity.ok(internalService.checkToken(token));
    }

    @PostMapping("/generateTokenForEmail")
    public ResponseEntity<String> generateTokenForEmail(@RequestBody String email){
        return ResponseEntity.ok(emailRequestTokenService.generateToken(email));
    }

    @PostMapping("/retrieveEmail")
    public ResponseEntity<String> retrieveEmail(@RequestBody String token){
        return ResponseEntity.ok(emailRequestTokenService.retrieveEmail(token));
    }
}
