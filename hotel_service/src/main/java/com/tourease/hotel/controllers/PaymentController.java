package com.tourease.hotel.controllers;

import com.tourease.hotel.models.dto.requests.MarkPaymentVO;
import com.tourease.hotel.models.dto.requests.NewPaymentVO;
import com.tourease.hotel.models.entities.Payment;
import com.tourease.hotel.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/hotel/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(description = "Create payment",
            summary = "Create payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful saved room")
    })
    @PostMapping("/worker/createPayment")
    public ResponseEntity<Void> createPayment(@RequestBody NewPaymentVO paymentCreateVO, @RequestHeader Long workerId) {
        paymentService.createNewPayment(paymentCreateVO, workerId);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Get all payments by customer",
            summary = "Get all payments by customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get all payments by customer")
    })
    @GetMapping("/worker/getAllPaymentsByCustomersForHotel")
    public ResponseEntity<List<Payment>> getAllPaymentsByCustomersForHotel(@RequestHeader List<Long> customers, @RequestHeader Long hotelId, @RequestHeader Long reservationNumber, @RequestParam Boolean isPaid) {
        return ResponseEntity.ok(paymentService.getAllPaymentsByCustomersForHotel(customers, hotelId, reservationNumber, isPaid));
    }

    @Operation(description = "Mark payment as paid",
            summary = "Mark payment as paid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful mark payment as paid")
    })
    @PutMapping("/worker/markPaymentAsPaid")
    public ResponseEntity<Void> markPaymentAsPaid(@RequestHeader Long workerId, @RequestBody MarkPaymentVO markPaymentVO) {
        paymentService.markPaymentAsPaid(markPaymentVO, workerId);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Delete payment by id",
            summary = "Delete payment by idr")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful delete payment by id")
    })
    @DeleteMapping("/worker/deletePaymentById/{paymentId}")
    public ResponseEntity<Void> deletePaymentById(@RequestHeader Long workerId, @PathVariable Long paymentId) {
        paymentService.deletePaymentById(paymentId, workerId);
        return ResponseEntity.ok().build();
    }
}
