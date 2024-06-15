package com.tourease.email.models.dto;

public record EmailInfoVO(String emailFrom,
                          String emailPassword,
                          String activateProfileURL,
                          String passportExpiredURL,
                          String changePasswordURL) {
}
