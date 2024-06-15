package com.tourease.user.controllers;

import com.tourease.configuration.exception.ErrorResponse;
import com.tourease.user.models.dto.request.ChangePasswordVO;
import com.tourease.user.models.dto.request.UserRegistration;
import com.tourease.user.models.dto.response.LoginResponse;
import com.tourease.user.models.dto.response.UserVO;
import com.tourease.user.services.UserService;
import com.tourease.user.services.communication.EmailServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final EmailServiceClient emailServiceClient;

    @Operation(hidden = true)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authorizeLogin(@RequestParam String username, String password) {
        return ResponseEntity.ok(userService.authorize(username, password));
    }

    @Operation(summary = "Register user.",
            description = "Body of user should have an unique and not blank email, " +
                    "password should not be blank as well.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, register the profile."),
            @ApiResponse(responseCode = "400", description = "Email already registered or not valid email.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@RequestBody @Valid UserRegistration userRegistration) {
        userService.register(userRegistration);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Send activation email.",
            description = "Used for resending activation link if not received one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully send email.")})
    @PostMapping("/sendActivateEmail/{email}")
    public ResponseEntity<Void> sendActivateEmail(@PathVariable String email) {
        emailServiceClient.checkConnection("Couldn't send activation email.");
        emailServiceClient.sendActivationMail(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Activate registered user.",
            description = "Used for profile activation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, activate the profile."),
            @ApiResponse(responseCode = "400",
                    description = "Profile not found, already activated profile or profile is inactive.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/activateUser/{email}")
    public ResponseEntity<Void> activateUser(@PathVariable String email) {
        userService.activateUser(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get logged user information.",
            description = "Used to retrieve user role and email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the profile info."),
            @ApiResponse(responseCode = "400", description = "Profile not found.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/getLoggedUser/{email}")
    public ResponseEntity<UserVO> getLoggedUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.getLoggedUser(email));
    }

    @Operation(summary = "Send email for password change.",
            description = "When the user had forgotten his password, can receive email with change password endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, the email has been send."),
            @ApiResponse(responseCode = "400", description = "Profile with such email doesn't exist.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/sendPasswordChangeEmail/{email}")
    public ResponseEntity<Void> sendPasswordChangeEmail(@PathVariable String email) {
        userService.sendPasswordChangeLink(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change user password.",
            description = "Used to change user password with new one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully, saved the new password."),
            @ApiResponse(responseCode = "400", description = "User not found.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordVO changePasswordVO) {
        userService.changePassword(changePasswordVO);
        return ResponseEntity.ok().build();
    }
}
