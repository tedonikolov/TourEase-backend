package com.tourease.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tourease.core.controllers.ReservationController;
import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.ReservationCreateDTO;
import com.tourease.core.models.dto.ReservationDTO;
import com.tourease.core.models.dto.ReservationsFilter;
import com.tourease.core.models.enums.Currency;
import com.tourease.core.models.enums.ReservationStatus;
import com.tourease.core.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
public class ReservationControllerIntegrationTest {

    @InjectMocks
    private ReservationController reservationController;

    @MockBean
    private ReservationService reservationService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule here
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateReservation() throws Exception {
        ReservationCreateDTO reservationCreateDTO = new ReservationCreateDTO(
                1L, 1L, 1L, 1L, OffsetDateTime.now(), OffsetDateTime.now().plusDays(1), 1, BigDecimal.valueOf(100), 1, Currency.USD
        );
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/createReservation")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationCreateDTO)))
                .andExpect(status().isOk());

        verify(reservationService).createReservation(any(ReservationCreateDTO.class), anyLong());
    }

    @Test
    public void testGetReservations() throws Exception {
        Long userId = 1L;
        String hotel = "Hotel ABC";
        Long reservationNumber = 123L;
        ReservationStatus status = ReservationStatus.CONFIRMED;
        LocalDate creationDate = LocalDate.now();
        LocalDate checkIn = LocalDate.now();
        int page = 1;
        int size = 10;
        IndexVM<ReservationDTO> reservations = new IndexVM<>();

        when(reservationService.getReservations(anyLong(), any(ReservationsFilter.class), anyInt(), anyInt()))
                .thenReturn(reservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/getReservations")
                        .header("userId", userId)
                        .param("hotel", hotel)
                        .param("reservationNumber", reservationNumber.toString())
                        .param("status", status.name())
                        .param("creationDate", creationDate.toString())
                        .param("checkIn", checkIn.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(reservationService).getReservations(anyLong(), any(ReservationsFilter.class), anyInt(), anyInt());
    }

    @Test
    public void testCancelReservation() throws Exception {
        Long reservationId = 123L;

        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/cancelReservation")
                        .header("reservationId", reservationId))
                .andExpect(status().isOk());

        verify(reservationService).cancelReservation(anyLong());
    }
}
