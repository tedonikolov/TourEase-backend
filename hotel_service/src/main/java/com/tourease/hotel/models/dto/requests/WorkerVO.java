package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.validations.annotations.PhoneField;

public record WorkerVO(Long id,
                       String fullName,
                       String email,
                       @PhoneField
                       String phone,
                       WorkerType workerType,
                       Long hotelId) {
}
