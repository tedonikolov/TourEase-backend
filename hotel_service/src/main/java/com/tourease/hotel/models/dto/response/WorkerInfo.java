package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Worker;
import com.tourease.hotel.models.enums.WorkerType;

public record WorkerInfo(Long id, String fullName, String email, Hotel hotel, WorkerType workerType) {
    public WorkerInfo(Worker worker){
        this(worker.getId(), worker.getFullName(), worker.getEmail(), worker.getHotel(), worker.getWorkerType());
    }
}
