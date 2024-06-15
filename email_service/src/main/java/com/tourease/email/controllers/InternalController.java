package com.tourease.email.controllers;

import com.tourease.email.models.dto.PaymentChangeReservationVO;
import com.tourease.email.models.dto.ReservationConfirmationVO;
import com.tourease.email.models.dto.ReservationDeclinedVO;
import com.tourease.email.services.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@AllArgsConstructor
public class InternalController {
    private final EmailSenderService emailSenderService;

    @PostMapping("/sendReservationConfirmation")
    public ResponseEntity<Void> sendReservationConfirmation(@RequestBody ReservationConfirmationVO reservationConfirmationVO){
        emailSenderService.sendReservationConfirmation(reservationConfirmationVO);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/sendDeclinedReservation")
    public ResponseEntity<Void> sendDeclinedReservation(@RequestBody ReservationDeclinedVO reservationDeclinedVO){
        emailSenderService.sendDeclinedReservation(reservationDeclinedVO.email(), reservationDeclinedVO.name(), reservationDeclinedVO.reservationNumber());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendPaymentChangeReservation")
    public ResponseEntity<Void> sendPaymentChangeReservation(@RequestBody PaymentChangeReservationVO paymentChangeReservationVO){
        emailSenderService.sendPaymentChangeReservation(paymentChangeReservationVO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendActivationMail")
    public ResponseEntity<Void> sendActivationMail(@RequestParam String email){
        emailSenderService.sendActivationMail(email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/sendPasswordChangeLink")
    public ResponseEntity<Void> sendPasswordChangeLink(@RequestParam String email){
        emailSenderService.sendPasswordChangeLink(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendPassportDateExpiredNotify")
    public ResponseEntity<Void> sendPassportDateExpiredNotify(@RequestParam String email, @RequestParam String fullName){
        emailSenderService.sendPassportDateExpiredNotify(email, fullName);
        return ResponseEntity.ok().build();
    }
}
