package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(description = "Get customer by passport id",
            summary = "Returns the information of reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved room")
    })
    @GetMapping("/getCustomerByPassportId/{passportId}")
    public ResponseEntity<CustomerDTO> getCustomerByPassportId(@PathVariable String passportId) {
        return ResponseEntity.ok(customerService.getCustomerByPassportId(passportId));
    }
}
