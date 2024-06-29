package com.tourease.core;

import com.netflix.discovery.EurekaClient;
import com.tourease.core.controllers.SearchController;
import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.HotelPreview;
import com.tourease.core.models.dto.RoomVO;
import com.tourease.core.services.SearchService;
import com.tourease.core.services.communication.HotelServiceClient;
import com.tourease.core.services.communication.TranslaterApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SearchController.class)
public class SearchControllerIntegrationTest {
    @MockBean
    private SearchService searchService;

    @MockBean
    private HotelServiceClient hotelServiceClient;

    @MockBean
    private TranslaterApiClient translaterApiClient;

    @MockBean
    private EurekaClient eurekaClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetHotelListing() throws Exception {
        // Arrange
        String searchText = "test";
        int page = 1;
        IndexVM<HotelPreview> mockResponse = new IndexVM<>();
        when(searchService.listing(searchText, page)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/search/listing")
                        .param("page", String.valueOf(page)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNotAvailableDates() throws Exception {
        Long hotelId = 1L;
        Long typeId = 2L;
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(7);
        List<LocalDate> mockDates = List.of(fromDate, toDate);
        when(hotelServiceClient.getNotAvailableDates(hotelId, typeId, fromDate, toDate)).thenReturn(mockDates);

        mockMvc.perform(MockMvcRequestBuilders.get("/search/getNotAvailableDates")
                        .param("hotelId", String.valueOf(hotelId))
                        .param("typeId", String.valueOf(typeId))
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").value(fromDate.toString()))
                .andExpect(jsonPath("$[1]").value(toDate.toString()));
    }

    @Test
    public void testGetFreeRoomsForDateByTypeId() throws Exception {
        Long hotelId = 1L;
        Long typeId = 2L;
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(7);
        List<RoomVO> mockRooms = List.of(new RoomVO(1L, "101"), new RoomVO(2L, "102"));
        when(hotelServiceClient.getFreeRoomsBetweenDateByTypeId(hotelId, typeId, fromDate, toDate)).thenReturn(mockRooms);

        mockMvc.perform(MockMvcRequestBuilders.get("/search/getFreeRoomsForDateByTypeId")
                        .header("hotelId", String.valueOf(hotelId))
                        .header("typeId", String.valueOf(typeId))
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("101"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("102"));
    }
}

