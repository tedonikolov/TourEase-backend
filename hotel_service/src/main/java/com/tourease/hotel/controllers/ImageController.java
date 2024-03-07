package com.tourease.hotel.controllers;

import com.tourease.hotel.models.entities.Image;
import com.tourease.hotel.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/hotel/image")
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @Operation(description = "Gets hotel image by id",
            summary = "Gets hotel image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully return the image")
    })
    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId){
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/webp")).body(imageService.getImage(imageId));
    }

    @Operation(description = "Gets all hotel images",
            summary = "Gets all hotel images urls")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returns urls")
    })
    @GetMapping("/getForHotel/{hotelId}")
    public ResponseEntity<List<String>> getImages(@PathVariable Long hotelId){
        return ResponseEntity.ok(imageService.getImages(hotelId));
    }

    @Operation(description = "Add hotel image",
            summary = "Add image to hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added image"),
            @ApiResponse(responseCode = "500", description = "Biggest size image",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Image> addImage(@RequestParam MultipartFile image, @RequestParam Long hotelId){
        return ResponseEntity.ok(imageService.saveImage(image,hotelId));
    }

    @Operation(description = "Delete hotel image",
            summary = "Removes image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed image"),
    })
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId){
        imageService.deleteImage(imageId);
        return ResponseEntity.ok().build();
    }
}
