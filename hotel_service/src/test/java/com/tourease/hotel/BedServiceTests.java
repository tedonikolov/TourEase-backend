package com.tourease.hotel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.tourease.hotel.models.dto.requests.BedVO;
import com.tourease.hotel.models.entities.Bed;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Type;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.repositories.BedRepository;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.services.BedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BedServiceTests {

    @Mock
    private BedRepository bedRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BedService bedService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewBed() {
        BedVO bedVO = new BedVO(0L, "Single Bed", 1, BigDecimal.valueOf(50), 1L, Currency.BGN);
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bedRepository.save(any(Bed.class))).thenReturn(new Bed());

        bedService.save(bedVO);

        verify(bedRepository, times(1)).save(any(Bed.class));
    }

    @Test
    public void testSaveExistingBed() {
        BedVO bedVO = new BedVO(1L, "Single Bed", 1, BigDecimal.valueOf(50), 1L, Currency.BGN);
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        Bed bed = new Bed();
        bed.setId(1L);
        bed.setHotel(hotel);

        Type type = new Type();
        type.setBeds(List.of(bed));
        bed.setTypes(Set.of(type));

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bedRepository.findById(1L)).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        bedService.save(bedVO);

        verify(bedRepository, times(1)).save(any(Bed.class));
        assertEquals(BigDecimal.valueOf(50.0), type.getPrice());
    }

    @Test
    public void testDeleteBed() {
        bedService.delete(1L);
        verify(bedRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSaveBedHotelNotFound() {
        BedVO bedVO = new BedVO(0L, "Single Bed", 1, BigDecimal.valueOf(100), 1L, Currency.USD);

        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bedService.save(bedVO));
    }

    @Test
    public void testSaveBedNotFound() {
        BedVO bedVO = new BedVO(1L, "Single Bed", 1, BigDecimal.valueOf(100), 1L, Currency.USD);
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bedRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bedService.save(bedVO));
    }
}
