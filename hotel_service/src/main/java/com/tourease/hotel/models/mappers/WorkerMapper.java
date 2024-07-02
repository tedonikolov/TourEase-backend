package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.WorkerVO;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Worker;

public class WorkerMapper {
    public static Worker toEntity(Long id, WorkerVO vo, Hotel hotel){
        return Worker.builder()
                .id(id)
                .email(vo.email())
                .phone(vo.phone())
                .fullName(vo.fullName())
                .workerType(vo.workerType())
                .hotel(hotel)
                .build();
    }

    public static void updateEntity(Worker worker, WorkerVO workerVO) {
        worker.setFullName(workerVO.fullName());
        worker.setPhone(workerVO.phone());
        worker.setWorkerType(workerVO.workerType());
    }
}
