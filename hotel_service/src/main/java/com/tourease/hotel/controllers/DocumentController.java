package com.tourease.hotel.controllers;

import com.tourease.hotel.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "docs")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @Operation(summary = "Creates and returns a pdf for invoice details.")
    @PostMapping(path = "/reservation", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getInvoice(HttpServletResponse response, @RequestParam Long reservationId, @RequestParam Long userId, @RequestParam String language) {
        documentService.generateInvoicePdf(response, userId, reservationId, language);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "invoice.pdf" + "\"").build();
    }
}
