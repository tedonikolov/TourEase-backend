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
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String hotelAppName = "HOTEL-SERVICE";
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
                .queryParam("pageNumber", filterHotelListing.getPageNumber())
                .queryParam("currency", filterHotelListing.getCurrency());

        return defaultRestTemplate.getForObject(builder.toUriString(), IndexVM.class);
    }

    public TakenDaysForType getNotAvailableDates(Long typeId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hotelServiceUrl + "/internal/getNotAvailableDates")
                .queryParam("typeId", typeId);

        return defaultRestTemplate.getForObject(builder.toUriString(), TakenDaysForType.class);
    }

    public Long createReservation(ReservationCreateDTO reservationCreateDTO, UserVO userVO) {
        return defaultRestTemplate.postForObject(hotelServiceUrl + "/internal/createReservation", new ReservationVO(reservationCreateDTO, userVO), Long.class);
    }

    public HotelVO getHotel(Long number) {
        return defaultRestTemplate.getForObject(hotelServiceUrl + "/internal/getHotelByReservationNumber/" + number, HotelVO.class);
    }

    public void cancelReservation(Long reservationNumber) {
        defaultRestTemplate.put(hotelServiceUrl + "/internal/cancelReservation/"+reservationNumber, Void.class);
    }

    public void rateHotel(RatingVO rating) {
        defaultRestTemplate.postForObject(hotelServiceUrl + "/internal/rate", rating, Void.class);
    }

    public List<RoomVO> getFreeRoomsBetweenDateByTypeId(Long hotelId, Long typeId, LocalDate fromDate, LocalDate toDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hotelServiceUrl + "/internal/getFreeRoomsForDateByTypeId")
                .queryParam("hotelId", hotelId)
                .queryParam("typeId", typeId)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate);

        return Arrays.stream(defaultRestTemplate.getForObject(builder.toUriString(), RoomVO[].class)).toList();
    }
}
