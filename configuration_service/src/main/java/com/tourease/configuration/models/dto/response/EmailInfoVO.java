package com.tourease.configuration.models.dto.response;

public record EmailInfoVO(String emailFrom,
                          String emailPassword,
                          String activateProfileURL,
                          String passportExpiredURL,
                          String changePasswordURL) {
}
