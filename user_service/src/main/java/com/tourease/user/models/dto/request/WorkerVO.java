package com.tourease.user.models.dto.request;

import com.tourease.user.models.enums.WorkerType;

public record WorkerVO(String email, WorkerType workerType) {
}
