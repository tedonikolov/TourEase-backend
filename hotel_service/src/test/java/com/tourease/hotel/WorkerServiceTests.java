package com.tourease.hotel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.hotel.models.dto.requests.WorkerVO;
import com.tourease.hotel.models.dto.response.WorkerInfo;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Worker;
import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.WorkerRepository;
import com.tourease.hotel.services.WorkerService;
import com.tourease.hotel.services.communication.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WorkerServiceTests {

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private EurekaClient eurekaClient;


    @InjectMocks
    private WorkerService workerService;

    private Worker worker;
    private WorkerVO workerVO;
    private Hotel hotel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = new Hotel();
        hotel.setId(1L);

        worker = Worker.builder()
                .id(1L)
                .email("worker@example.com")
                .workerType(WorkerType.RECEPTIONIST)
                .phone("+35988847571")
                .fullName("Nasko Petrov")
                .hotel(hotel)
                .build();

        workerVO = new WorkerVO(1L, "Nasko Petrov","worker@example.com", "+35988847571",WorkerType.RECEPTIONIST, 1L);
    }

    @Test
    public void testSaveNewWorker() {
        when(eurekaClient.getApplication("user-service")).thenReturn(new Application("USER-SERVICE"));
        when(userServiceClient.createWorkerUser(workerVO.email(), workerVO.workerType())).thenReturn(2L);
        when(hotelRepository.findById(workerVO.hotelId())).thenReturn(Optional.of(hotel));
        workerVO = new WorkerVO(0L, "Nasko Petrov","worker@example.com", "+35988847571",WorkerType.RECEPTIONIST, 1L);

        workerService.save(workerVO);

        verify(userServiceClient, times(1)).createWorkerUser(workerVO.email(), workerVO.workerType());
        verify(workerRepository, times(1)).save(any(Worker.class));
        verify(userServiceClient, times(1)).checkConnection();
    }

    @Test
    public void testSaveExistingWorker() {
        when(workerRepository.findById(workerVO.id())).thenReturn(Optional.of(worker));

        workerService.save(workerVO);

        verify(workerRepository, times(1)).save(worker);
    }

    @Test
    public void testDeleteWorker() {
        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));

        workerService.delete(1L);

        verify(userServiceClient, times(1)).checkConnection();
        verify(userServiceClient, times(1)).fireWorker(1L);
        verify(workerRepository, times(1)).save(worker);
        assertEquals(LocalDate.now(), worker.getFiredDate());
    }

    @Test
    public void testReassignWorker() {
        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));

        workerService.reassign(1L);

        verify(userServiceClient, times(1)).checkConnection();
        verify(userServiceClient, times(1)).reassignWorker(1L);
        verify(workerRepository, times(1)).save(worker);
        assertEquals(LocalDate.now(), worker.getRegistrationDate());
        assertNull(worker.getFiredDate());
    }

    @Test
    public void testGetWorkerByEmail() {
        when(workerRepository.findByEmail("worker@example.com")).thenReturn(worker);

        WorkerInfo workerInfo = workerService.getWorkerByEmail("worker@example.com");

        assertNotNull(workerInfo);
        assertEquals("worker@example.com", workerInfo.email());
    }

    @Test
    public void testFindById() {
        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));

        Worker foundWorker = workerService.findById(1L);

        assertNotNull(foundWorker);
        assertEquals("worker@example.com", foundWorker.getEmail());
    }

    @Test
    public void testFindByIdNotFound() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            workerService.findById(1L);
        });

        assertEquals("Worker not found", exception.getMessage());
    }
}
