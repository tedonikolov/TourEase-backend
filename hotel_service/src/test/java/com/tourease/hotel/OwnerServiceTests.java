package com.tourease.hotel;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.OwnerSaveVO;
import com.tourease.hotel.models.dto.response.OwnerInfo;
import com.tourease.hotel.models.entities.Owner;
import com.tourease.hotel.repositories.OwnerRepository;
import com.tourease.hotel.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

public class OwnerServiceTests {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOwner() {
        Long id = 1L;
        String email = "test@example.com";

        when(ownerRepository.findById(id)).thenReturn(Optional.empty());
        when(ownerRepository.save(any(Owner.class))).thenReturn(new Owner(id, email));

        ownerService.createOwner(id, email);

        verify(ownerRepository, times(1)).save(any(Owner.class));
        verify(kafkaTemplate, times(1)).send("hotel_service", email, "Owner created.");
    }

    @Test
    public void testSaveOwner() {
        OwnerSaveVO newOwner = new OwnerSaveVO(1L, "Ivan Petrov", "Company", "Address", "1234567890", "USA", "City", "EIK");
        Owner existingOwner = new Owner(1L, "test@example.com");

        when(ownerRepository.findById(newOwner.id())).thenReturn(Optional.of(existingOwner));
        when(ownerRepository.save(existingOwner)).thenReturn(existingOwner);

        ownerService.saveOwner(newOwner);

        verify(ownerRepository, times(1)).save(existingOwner);
        verify(kafkaTemplate, times(1)).send("hotel_service", existingOwner.getEmail(), "Updated owner information.");
    }

    @Test
    public void testFindOwnerByEmail() {
        String email = "test@example.com";
        Owner owner = new Owner(1L, email);

        when(ownerRepository.findByEmail(email)).thenReturn(Optional.of(owner));

        OwnerInfo ownerInfo = ownerService.findOwnerByEmail(email);

        assertEquals(email, ownerInfo.email());
    }

    @Test
    public void testFindOwnerById() {
        Long id = 1L;
        Owner owner = new Owner(id, "test@example.com");

        when(ownerRepository.findById(id)).thenReturn(Optional.of(owner));

        Owner foundOwner = ownerService.findOwnerById(id);

        assertEquals(id, foundOwner.getId());
    }

    @Test
    public void testFindOwnerById_NotFound() {
        Long id = 1L;

        when(ownerRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> ownerService.findOwnerById(id));

        assertEquals(ErrorCode.EntityNotFound, exception.getErrorCode());
    }
}
