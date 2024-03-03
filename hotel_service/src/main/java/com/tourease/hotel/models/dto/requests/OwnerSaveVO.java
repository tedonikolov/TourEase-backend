package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.validations.annotations.Phone;

@Phone
public record OwnerSaveVO(Long id,
                          String fullName,
                          String companyName,
                          String companyAddress,
                          String phone,
                          String country,
                          String city,
                          String eik) {
}
