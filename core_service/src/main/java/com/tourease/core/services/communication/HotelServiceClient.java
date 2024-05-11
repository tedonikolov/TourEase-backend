package com.tourease.core.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String hotelAppName = "CONFIGURATION-SERVICE";
    private final String hotelServiceUrl = "http://hotel-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(hotelAppName);
        if (service == null)
            throw new InternalServiceException("No connection to hotel-service.");
    }

    public DataSet getDataSet() {
        return defaultRestTemplate.getForObject(hotelServiceUrl + "/internal/getDataSet", DataSet.class);
    }

    public IndexVM<HotelPreview> getHotels(FilterHotelListing filterHotelListing) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hotelServiceUrl + "/hotel/listing")
                .queryParam("country", filterHotelListing.getCountry() !=null ? filterHotelListing.getCountry():null)
                .queryParam("city", filterHotelListing.getCity() !=null ? filterHotelListing.getCity():null)
                .queryParam("name", filterHotelListing.getName() !=null ? filterHotelListing.getName():null)
                .queryParam("stars", filterHotelListing.getStars())
                .queryParam("mealType", filterHotelListing.getMealType())
                .queryParam("facilities", filterHotelListing.getFacilities()!=null  ? filterHotelListing.getFacilities():null)
                .queryParam("people", filterHotelListing.getPeople())
                .queryParam("fromPrice", filterHotelListing.getFromPrice())
                .queryParam("toPrice", filterHotelListing.getToPrice())
                .queryParam("fromDate", filterHotelListing.getFromDate())
                .queryParam("toDate", filterHotelListing.getToDate())
                .queryParam("pageNumber", filterHotelListing.getPageNumber());

        return defaultRestTemplate.getForObject(builder.toUriString(), IndexVM.class);
    }

    public List<LocalDate> getNotAvailableDates(Long hotelId, Long typeId, LocalDate fromDate, LocalDate toDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hotelServiceUrl + "/internal/getNotAvailableDates")
                .queryParam("hotelId", hotelId)
                .queryParam("typeId", typeId)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate);

        return defaultRestTemplate.getForObject(builder.toUriString(), List.class);
    }

    public void createReservation(ReservationCreateDTO reservationCreateDTO, UserVO userVO) {
        defaultRestTemplate.postForObject(hotelServiceUrl + "/internal/createReservation", new ReservationVO(reservationCreateDTO, userVO), ReservationVO.class);
    }
}
