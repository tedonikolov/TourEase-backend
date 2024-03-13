package com.tourease.user.models.dto.request;

public record EmailInfoVO(String emailFrom,
                          String emailPassword,
                          String activateProfileURL,
                          String passportExpiredURL,
                          String changePasswordURL) {
}
