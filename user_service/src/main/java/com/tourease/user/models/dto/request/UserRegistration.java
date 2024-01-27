package com.tourease.user.models.dto.request;

import com.tourease.user.models.enums.UserType;
import com.tourease.user.validation.annotation.Email;
import com.tourease.user.validation.annotation.ExistingEmail;

public record UserRegistration(
        @Email
        @ExistingEmail
        String email,
        String password,
        UserType userType) {
}
