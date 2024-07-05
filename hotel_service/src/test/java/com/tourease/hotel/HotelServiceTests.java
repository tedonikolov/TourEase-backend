package com.tourease.hotel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.CustomException;
import com.tourease.hotel.models.custom.IndexVM;
import com.tourease.hotel.models.dto.requests.FilterHotelListing;
import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.models.dto.requests.HotelWorkingPeriodVO;
import com.tourease.hotel.models.dto.response.CurrencyRateVO;
import com.tourease.hotel.models.dto.response.HotelPreview;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Location;
import com.tourease.hotel.models.entities.Owner;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.Stars;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.ImageRepository;
import com.tourease.hotel.repositories.ReservationRepository;
import com.tourease.hotel.services.HotelService;
import com.tourease.hotel.services.LocationService;
import com.tourease.hotel.services.OwnerService;
import com.tourease.hotel.services.communication.ConfigServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

public class HotelServiceTests {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private OwnerService ownerService;

    @Mock
    private LocationService locationService;

    @Mock
    private ConfigServiceClient configServiceClient;

    @Mock
    private EurekaClient eurekaClient;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private HotelCreateVO hotelCreateVO;
    private Owner owner;
    private Location location;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new Owner();
        owner.setId(1L);
        owner.setEmail("owner@example.com");

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel Test");
        hotel.setOwner(owner);

        location = new Location(1L,BigDecimal.valueOf(36),BigDecimal.valueOf(23),"address","Golden Sands","country",hotel);
        hotel.setLocation(location);

        hotelCreateVO = new HotelCreateVO(0L, "Hotel Test", Currency.USD, Stars.THREE, location, 1L);
    }

    @Test
    public void testSaveNewHotel() {
        when(ownerService.findOwnerById(hotelCreateVO.ownerId())).thenReturn(owner);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        hotelService.save(hotelCreateVO);

        verify(ownerService, times(1)).findOwnerById(hotelCreateVO.ownerId());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
        verify(locationService, times(1)).save(any(Location.class));
        verify(kafkaTemplate, times(1)).send("hotel_service", owner.getEmail(), "Hotel created with name:" + hotelCreateVO.name());
    }

    @Test
    public void testSaveExistingHotel() {
        hotelCreateVO = new HotelCreateVO(1L, "Hotel Test", Currency.USD, Stars.THREE, location, 1L);
        when(hotelRepository.findById(hotelCreateVO.id())).thenReturn(Optional.of(hotel));

        hotelService.save(hotelCreateVO);

        verify(hotelRepository, times(1)).findById(hotelCreateVO.id());
        verify(hotelRepository, times(1)).save(hotel);
        verify(kafkaTemplate, times(1)).send("hotel_service", owner.getEmail(), "Hotel updated with name:" + hotelCreateVO.name());
    }

    @Test
    public void testChangeWorkingPeriod() {
        HotelWorkingPeriodVO workingPeriodVO = new HotelWorkingPeriodVO(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        when(hotelRepository.findById(workingPeriodVO.hotelId())).thenReturn(Optional.of(hotel));

        hotelService.changeWorkingPeriod(workingPeriodVO);

        verify(hotelRepository, times(1)).findById(workingPeriodVO.hotelId());
        verify(hotelRepository, times(1)).save(hotel);
        assertEquals(LocalDate.of(2024, 1, 1), hotel.getOpeningDate());
        assertEquals(LocalDate.of(2024, 12, 31), hotel.getClosingDate());
    }

    @Test
    public void testFindById() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel foundHotel = hotelService.findById(1L);

        assertNotNull(foundHotel);
        assertEquals("Hotel Test", foundHotel.getName());
    }

    @Test
    public void testFindByIdNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomException.class, () -> {
            hotelService.findById(1L);
        });

        assertEquals("Hotel not found", exception.getMessage());
    }

    @Test
    public void testListing() {
        when(eurekaClient.getApplication("CONFIGURATION-SERVICE")).thenReturn(new Application("CONFIGURATION-SERVICE"));
        when(configServiceClient.getCurrencyRates()).thenReturn(List.of(
                new CurrencyRateVO(Currency.BGN, BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0),
                        BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0),
                        BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0))));

        FilterHotelListing filter = new FilterHotelListing();
        filter.setPageNumber(1);
        filter.setFromPrice(BigDecimal.valueOf(100));
        filter.setToPrice(BigDecimal.valueOf(200));
        filter.setCity("Golden Sands");
        filter.setCurrency(Currency.BGN);

        List<Hotel> hotelsList = Arrays.asList(hotel);
        when(hotelRepository.findHotelByFilter(null, "Golden Sands", null, null, null,null))
                .thenReturn(hotelsList);
        when(reservationRepository.isRoomTaken(anyLong(), any(), any())).thenReturn(null);
        when(imageRepository.findByHotel_Id(anyLong())).thenReturn(Collections.emptyList());

        IndexVM<HotelPreview> result = hotelService.listing(filter);
        assertEquals(0, result.getItems().size());

        hotel.setTypes(Set.of(new Type(1L,"staq 1", BigDecimal.valueOf(120), Currency.BGN,null,null,hotel,null)));

        when(hotelRepository.findHotelByFilter(null, "Golden Sands", null, null, null,null))
                .thenReturn(Arrays.asList(hotel));

        IndexVM<HotelPreview> result2 = hotelService.listing(filter);

        assertNotNull(result2);
        assertEquals(1, result2.getItems().size());
        verify(hotelRepository, times(2)).findHotelByFilter(null, "Golden Sands", null, null, null, null);

        filter.setCity("Varna");

        IndexVM<HotelPreview> result3 = hotelService.listing(filter);
        assertEquals(0, result3.getItems().size());
    }
}
