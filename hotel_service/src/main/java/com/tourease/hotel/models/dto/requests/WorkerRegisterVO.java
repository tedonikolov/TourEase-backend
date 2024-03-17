package com.tourease.hotel.models.dto.requests;

import com.tourease.hotel.models.enums.WorkerType;

public record WorkerRegisterVO(String email, WorkerType workerType) {
}
