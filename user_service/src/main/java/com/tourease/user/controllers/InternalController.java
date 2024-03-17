package com.tourease.user.controllers;

import com.tourease.user.models.dto.request.WorkerVO;
import com.tourease.user.models.enums.UserType;
import com.tourease.user.models.enums.WorkerType;
import com.tourease.user.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("internal")
@AllArgsConstructor
@Hidden
public class InternalController {
    private final UserService userService;

    @PostMapping("/createUserForWorker")
    public ResponseEntity<Long> createUserForWorker(@RequestBody WorkerVO workerVO){
        return ResponseEntity.ok(userService.createUserForWorker(workerVO));
    }

    @PostMapping("/fireWorker/{id}")
    public ResponseEntity<Void> fireWorker(@PathVariable Long id){
        userService.makeInactive(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reassignWorker/{id}")
    public ResponseEntity<Void> reassignWorker(@PathVariable Long id){
        userService.makeActive(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeUserType/{id}")
    public ResponseEntity<Void> changeUserType(@PathVariable Long id, @RequestBody WorkerType workerType){
        userService.changeUserType(id, UserType.valueOf(workerType.name()));
        return ResponseEntity.ok().build();
    }
}
