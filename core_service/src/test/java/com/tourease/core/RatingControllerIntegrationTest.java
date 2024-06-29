package com.tourease.core;

import com.tourease.core.controllers.RatingController;
import com.tourease.core.models.dto.RatingVO;
import com.tourease.core.services.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RatingController.class)
public class RatingControllerIntegrationTest {

    @InjectMocks
    private RatingController ratingController;

    @MockBean
    private RatingService ratingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRateHotel() throws Exception {
        Long reservationId = 123L;
        RatingVO ratingVO = new RatingVO(1L,8.0,"Best hotel");

        mockMvc.perform(MockMvcRequestBuilders.post("/rating/rateHotel")
                        .header("reservationId", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hotelId\":1,\"rating\":8.0,\"comment\":\"Best hotel\"}"))
                .andExpect(status().isOk());

        verify(ratingService).rate(reservationId, ratingVO);
    }
}

