package com.tourease.core.models.dto;

import java.time.LocalDate;

public record CustomerVO(String fullName,
                         String phoneNumber,
                         String email,
                         String passportId,
                         LocalDate birthDate,
                         LocalDate creationDate,
                         LocalDate expirationDate,
                         String country,
                         String gender) {
    public CustomerVO(UserVO userVO) {
        this(   userVO.regular().firstName()+" "+userVO.regular().lastName(),
                userVO.regular().phone(),
                userVO.email(),
                userVO.regular().passport().passportId(),
                userVO.regular().birthDate(),
                userVO.regular().passport().creationDate(),
                userVO.regular().passport().expirationDate(),
                userVO.regular().country(),
                userVO.regular().gender());
    }
}