package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.custom.IndexVM;
import com.tourease.hotel.models.dto.requests.FilterHotelListing;
import com.tourease.hotel.models.dto.requests.HotelCreateVO;
import com.tourease.hotel.models.dto.requests.HotelWorkingPeriodVO;
import com.tourease.hotel.models.dto.response.CurrencyRateVO;
import com.tourease.hotel.models.dto.response.HotelPreview;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.mappers.HotelMapper;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.ImageRepository;
import com.tourease.hotel.repositories.ReservationRepository;
import com.tourease.hotel.services.communication.ConfigServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final ImageRepository imageRepository;
    private final ReservationRepository reservationRepository;
    private final OwnerService ownerService;
    private final LocationService locationService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ConfigServiceClient configServiceClient;

    public void save(HotelCreateVO hotelCreateVO) {
        if (hotelCreateVO.id() == 0) {
            Owner owner = ownerService.findOwnerById(hotelCreateVO.ownerId());
            Hotel hotel = hotelRepository.save(HotelMapper.toEntity(hotelCreateVO, owner));

            hotelCreateVO.location().setId(hotel.getId());
            locationService.save(hotelCreateVO.location());

            kafkaTemplate.send("hotel_service", owner.getEmail(), "Hotel created with name:" + hotelCreateVO.name());
        } else {
            Hotel hotel = findById(hotelCreateVO.id());

            HotelMapper.updateEntity(hotel, hotelCreateVO);
            hotelRepository.save(hotel);
            kafkaTemplate.send("hotel_service", hotel.getOwner().getEmail(), "Hotel updated with name:" + hotelCreateVO.name());
        }
    }

    public void changeWorkingPeriod(HotelWorkingPeriodVO hotelWorkingPeriodVO) {
        Hotel hotel = findById(hotelWorkingPeriodVO.hotelId());
        hotel.setOpeningDate(hotelWorkingPeriodVO.openingDate());
        hotel.setClosingDate(hotelWorkingPeriodVO.closingDate());
        hotelRepository.save(hotel);
    }

    public Hotel findById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new CustomException("Hotel not found", ErrorCode.EntityNotFound));
    }

    public IndexVM<HotelPreview> listing(FilterHotelListing filterHotelListing) {
        configServiceClient.checkConnection();
        List<CurrencyRateVO> currencyRates = configServiceClient.getCurrencyRates();

        filterHotelListing.decodeURL();

        List<Hotel> hotelsList = hotelRepository.findHotelByFilter(filterHotelListing.getCountry(),
                filterHotelListing.getCity(),
                filterHotelListing.getName(), filterHotelListing.getStars(),
                filterHotelListing.getFacilities(), filterHotelListing.getMealType());

        List<HotelPreview> hotels = new ArrayList<>();

        for (Hotel hotel : hotelsList) {
            Set<Type> types = hotel.getTypes().stream().filter(type -> filterTypePrice(type, filterHotelListing, currencyRates) && filterTypePeople(type, filterHotelListing)).collect(Collectors.toSet());
            Set<Meal> meals = filterHotelListing.getMealType() != null ?
                    hotel.getMeals().stream().filter(meal -> meal.getType().equals(filterHotelListing.getMealType())).collect(Collectors.toSet())
                    : hotel.getMeals();

            if (!types.isEmpty()) {
                HotelPreview hotelPreview = new HotelPreview(hotel, meals, types, filterHotelListing.getPeople() == null ? 1 : filterHotelListing.getPeople());
                hotelPreview.setFromDate(filterHotelListing.getFromDate());
                hotelPreview.setToDate(filterHotelListing.getToDate());
                hotels.add(hotelPreview);
            }
        }

        List<HotelPreview> listing = new ArrayList<>();

        if (hotels.size() > 10) {
            for (int i = filterHotelListing.getPageNumber() * 10 - 10; i < hotels.size(); i++) {
                if (filterHotelListing.getFromDate() != null && filterHotelListing.getToDate() != null) {
                    Set<Type> types = hotels.get(i).getTypes().stream().filter(type -> !type.getRooms().stream().filter(room ->
                                    reservationRepository.isRoomTaken(room.getId(), filterHotelListing.getFromDate(),
                                            filterHotelListing.getToDate()).isEmpty())
                            .collect(Collectors.toSet()).isEmpty()).collect(Collectors.toSet());
                    if (types.isEmpty())
                        continue;

                    hotels.get(i).setTypes(types);
                }

                List<Image> images = imageRepository.findByHotel_Id(hotels.get(i).getHotelId());
                hotels.get(i).setImages(images.stream().map(Image::getUrl).collect(Collectors.toList()));

                listing.add(hotels.get(i));
                if (listing.size() == 10)
                    break;
            }
        } else {
            for (HotelPreview hotel : hotels) {
                if (filterHotelListing.getFromDate() != null && filterHotelListing.getToDate() != null) {
                    Set<Type> types = hotel.getTypes().stream().filter(type -> !type.getRooms().stream().filter(room ->
                                    reservationRepository.isRoomTaken(room.getId(), filterHotelListing.getFromDate(),
                                            filterHotelListing.getToDate()).isEmpty())
                            .collect(Collectors.toSet()).isEmpty()).collect(Collectors.toSet());
                    if (types.isEmpty())
                        continue;
                    hotel.setTypes(types);
                }
                List<Image> images = imageRepository.findByHotel_Id(hotel.getHotelId());
                hotel.setImages(images.stream().map(Image::getUrl).collect(Collectors.toList()));
                listing.add(hotel);
            }
        }

        return new IndexVM<>(new PageImpl<>(listing, PageRequest.of(filterHotelListing.getPageNumber() - 1, 10), hotels.size()));
    }

    private boolean filterTypePrice(Type type, FilterHotelListing filter, List<CurrencyRateVO> currencyRates) {
        BigDecimal rate = Currency.getRate(filter.getCurrency(), type.getCurrency(),currencyRates);
        if (filter.getFromPrice() != null && filter.getToPrice() != null &&
                type.getPrice().multiply(rate).setScale(2, RoundingMode.HALF_UP).doubleValue() >= filter.getFromPrice().doubleValue() &&
                type.getPrice().multiply(rate).setScale(2, RoundingMode.HALF_UP).doubleValue() <= filter.getToPrice().doubleValue())
            return true;
        else {
            if (filter.getFromPrice() != null && filter.getToPrice() == null &&
                    type.getPrice().multiply(rate).setScale(2, RoundingMode.HALF_UP).doubleValue() >= filter.getFromPrice().doubleValue())
                return true;
            else if (filter.getToPrice() != null && filter.getFromPrice() == null &&
                    type.getPrice().multiply(rate).setScale(2, RoundingMode.HALF_UP).doubleValue() <= filter.getToPrice().doubleValue())
                return true;
            else
                return filter.getFromPrice() == null && filter.getToPrice() == null;
        }
    }

    private boolean filterTypePeople(Type type, FilterHotelListing filter) {
        if (filter.getPeople() == null)
            return true;
        else
            return type.getBeds().stream().mapToInt(Bed::getPeople).sum() >= filter.getPeople();
    }
}
