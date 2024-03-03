package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.OwnerSaveVO;
import com.tourease.hotel.models.dto.response.OwnerInfo;
import com.tourease.hotel.models.entities.Owner;
import com.tourease.hotel.models.mappers.OwnerMapper;
import com.tourease.hotel.repositories.OwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createOwner(Long id, String email) {
        ownerRepository.findById(id).orElse(ownerRepository.save(new Owner(id, email)));
        kafkaTemplate.send("hotel_service", email, "Owner created.");
    }

    public void saveOwner(OwnerSaveVO newOwner) {
        Owner owner = findOwnerById(newOwner.id());
        OwnerMapper.updateEntity(owner,newOwner);

        ownerRepository.save(owner);
        kafkaTemplate.send("hotel_service", owner.getEmail(), "Updated owner information.");
    }

    public OwnerInfo findOwnerByEmail(String email) {
        Owner owner = ownerRepository.findByEmail(email).orElseThrow(() -> new CustomException("Owner not found", ErrorCode.EntityNotFound));
        return new OwnerInfo(owner);
    }

    public Owner findOwnerById(Long id) {
        return ownerRepository.findById(id).orElseThrow(() -> new CustomException("Owner not found", ErrorCode.EntityNotFound));
    }
}