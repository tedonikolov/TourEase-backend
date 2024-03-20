package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.WorkerVO;
import com.tourease.hotel.models.dto.response.WorkerInfo;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Worker;
import com.tourease.hotel.models.mappers.WorkerMapper;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.WorkerRepository;
import com.tourease.hotel.services.communication.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final HotelRepository hotelRepository;
    private final UserServiceClient userServiceClient;

    public void save(WorkerVO workerVO) {
        Worker worker;

        if(workerVO.id()==0) {
            userServiceClient.checkConnection();
            Long id = userServiceClient.createWorkerUser(workerVO.email(), workerVO.workerType());

            Hotel hotel = hotelRepository.findById(workerVO.hotelId()).get();

            worker = WorkerMapper.toEntity(id, workerVO, hotel);
        } else {
            worker = workerRepository.findById(workerVO.id()).get();

            if(workerVO.workerType()!=worker.getWorkerType())
                userServiceClient.changeUserType(workerVO.id(), workerVO.workerType());

            WorkerMapper.updateEntity(worker,workerVO);
        }

        workerRepository.save(worker);
    }

    public void delete(Long id) {
        userServiceClient.checkConnection();
        userServiceClient.fireWorker(id);

        Worker worker = workerRepository.findById(id).get();
        worker.setFiredDate(LocalDate.now());

        workerRepository.save(worker);
    }

    public void reassign(Long id) {
        userServiceClient.checkConnection();
        userServiceClient.reassignWorker(id);

        Worker worker = workerRepository.findById(id).get();
        worker.setRegistrationDate(LocalDate.now());
        worker.setFiredDate(null);

        workerRepository.save(worker);
    }

    public WorkerInfo getWorkerByEmail(String email) {
        Worker worker = workerRepository.findByEmail(email);

        return new WorkerInfo(worker);
    }
}
