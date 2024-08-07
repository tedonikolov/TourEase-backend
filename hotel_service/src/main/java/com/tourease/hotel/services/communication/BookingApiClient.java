package com.tourease.hotel.services.communication;

import com.tourease.hotel.models.dto.response.*;
import com.tourease.hotel.models.entities.*;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.FacilityEnum;
import com.tourease.hotel.models.enums.RoomStatus;
import com.tourease.hotel.models.enums.Stars;
import com.tourease.hotel.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class BookingApiClient {
    @Qualifier("rapidApiRestTemplate")
    private final RestTemplate rapidApiRestTemplate;
    private final HotelRepository hotelRepository;
    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;
    private final ImageRepository imageRepository;
    private final FacilityRepository facilityRepository;

    public void getHotels() {
        ParameterizedTypeReference<ApiResponse<ListHotel>> listHotelResponseType = new ParameterizedTypeReference<>() {};
        for (int i=1;i<=20;i++) {
            ResponseEntity<ApiResponse<ListHotel>> hotels = rapidApiRestTemplate.exchange("https://booking-com15.p.rapidapi.com/api/v1/hotels/searchHotels?dest_id=33&search_type=COUNTRY&arrival_date=2024-06-07&departure_date=2024-06-08&page_number="+i, HttpMethod.GET, null, listHotelResponseType);

            for (ApiHotel hotel : hotels.getBody().data().hotels()) {
                //Vzemane na dobulnitelna informaciq za hotelite
                ParameterizedTypeReference<ApiResponse<HotelInfoVO>> hotelInfoResponseType = new ParameterizedTypeReference<>() {
                };
                HotelInfoVO hotelInfo = rapidApiRestTemplate.exchange("https://booking-com15.p.rapidapi.com/api/v1/hotels/getHotelDetails?hotel_id=" + hotel.hotel_id() + "&arrival_date=2024-06-05&departure_date=2024-06-13", HttpMethod.GET, null, hotelInfoResponseType).getBody().data();

                Hotel newHotel = new Hotel();
                newHotel.setName(hotel.property().name());
                newHotel.setStars(Stars.setValue(hotel.property().propertyClass()));
                newHotel.setCurrency(Currency.EUR);

                hotelRepository.save(newHotel);

                Location location = Location.builder()
                        .id(newHotel.getId())
                        .address(hotelInfo.address())
                        .city(hotelInfo.city())
                        .country(hotelInfo.country_trans())
                        .latitude(hotel.property().latitude())
                        .longitude(hotel.property().longitude())
                        .build();

                newHotel.setLocation(location);

                Rating rating = Rating.builder()
                        .id(newHotel.getId())
                        .rating(hotel.property().reviewScore())
                        .numberOfRates(hotel.property().reviewCount())
                        .totalRating(BigDecimal.valueOf(hotel.property().reviewCount() * hotel.property().reviewScore().floatValue()))
                        .build();

                newHotel.setRating(rating);

                hotelRepository.save(newHotel);

                hotelInfo.property_highlight_strip().forEach(facilityVO -> saveFacility(facilityVO.name(), newHotel));

                //Vzemane na informaciq za staite na hotela
                ParameterizedTypeReference<ApiResponse<RoomMapVO>> roomMapResponseType = new ParameterizedTypeReference<>() {
                };
                RoomMapVO roomMapVO = rapidApiRestTemplate.exchange("https://booking-com15.p.rapidapi.com/api/v1/hotels/getRoomList?hotel_id=" + hotel.hotel_id() + "&arrival_date=2024-06-06&departure_date=2024-06-12", HttpMethod.GET, null, roomMapResponseType).getBody().data();
                if(roomMapVO!=null && roomMapVO.rooms()!=null && roomMapVO.block()!=null) {
                    Map<String,ApiTypeVO> rooms = new HashMap<>();
                    for (RoomTypeVO roomType : roomMapVO.block()) {
                        if (rooms.containsKey(roomType.room_id())) {
                            ApiTypeVO typeCount = rooms.get(roomType.room_id());
                            typeCount.setCount(typeCount.getCount() + roomType.room_count());
                            if (roomType.nr_adults() + roomType.nr_children() > typeCount.getPeople())
                                typeCount.setPeople(roomType.nr_adults() + roomType.nr_children());
                            rooms.put(roomType.room_id(), typeCount);
                        } else {
                            rooms.put(roomType.room_id(), new ApiTypeVO(roomType.room_name(), roomType.room_count(), roomType.nr_adults()+roomType.nr_children()));
                        }
                    }

                    List<Type> types = new ArrayList<>();

                    for(Map.Entry<String,RoomVO> entry : roomMapVO.rooms().entrySet()) {
                        if (entry.getValue().bed_configurations() != null) {
                            //Zapazvane na vidovete legla na hotela
                            List<BedVO> beds = entry.getValue().bed_configurations().stream().flatMap(bedConfiguration -> bedConfiguration.bed_types().stream()).toList();
                            for (BedVO bed : beds) {
                                if (bedRepository.findByNameAndHotel_Id(recoginzeName(bed.bed_type()), newHotel.getId()).isEmpty()) {
                                    Random random = new Random();
                                    int price = random.nextInt(71) + 30; // Generates a random number between 0 and 70, then adds 30

                                    Bed newBed = Bed.builder()
                                            .people(recoginzePeople(bed.bed_type()))
                                            .price(BigDecimal.valueOf(price))
                                            .currency(Currency.EUR)
                                            .name(recoginzeName(bed.bed_type()))
                                            .hotel(newHotel)
                                            .build();

                                    bedRepository.save(newBed);
                                }
                            }

                            //Zapazvane na vidovete stai na hotela
                            for (BedConfiguration bedConfiguration : entry.getValue().bed_configurations()) {

                                List<Bed> bedsList = new ArrayList<>();
                                BigDecimal price = BigDecimal.valueOf(0);

                                for (BedVO bedVO : bedConfiguration.bed_types()) {

                                    Bed bed = bedRepository.findByNameAndHotel_Id(recoginzeName(bedVO.bed_type()), newHotel.getId()).get();
                                    for (int j = 0; j < bedVO.count(); j++) {
                                        bedsList.add(bed);

                                        price = price.add(bed.getPrice());
                                    }
                                }

                                Type type = Type.builder()
                                        .name(rooms.get(entry.getKey()).getName())
                                        .price(price)
                                        .currency(Currency.EUR)
                                        .beds(bedsList)
                                        .hotel(newHotel)
                                        .build();

                                types.add(type);
                                typeRepository.save(type);
                            }

                            for (PhotosVO photosVO : entry.getValue().photos().size()>2 ? entry.getValue().photos().subList(0,2) : entry.getValue().photos()) {
                                if (imageRepository.findByUrlAndHotel_Id(photosVO.url_original(), newHotel.getId()).isEmpty()) {
                                    Image image = Image.builder()
                                            .url(photosVO.url_original())
                                            .hotel(newHotel)
                                            .build();

                                    imageRepository.save(image);
                                }
                            }

                            entry.getValue().facilities().forEach(facilityVO -> saveFacility(facilityVO.name(), newHotel));
                        }
                    }

                    int j = 1;
                    for (ApiTypeVO apiTypeVO : rooms.values()) {
                        for (int k = 0; k < apiTypeVO.getCount(); k++) {
                            if(k==50)
                                break;
                            int number = (j*100+k);
                            List<Type> typeRoom = types.stream().filter(type -> type.getName().equals(apiTypeVO.getName())).toList();

                            roomRepository.save(new Room(Integer.toString(number), RoomStatus.FREE, typeRoom, newHotel));
                        }
                        j++;
                    }
                }
            }
        }
    }

    private int recoginzePeople(Integer integer) {
        return switch (integer) {
            case 2, 3, 6 -> 2;
            default -> 1;
        };
    }

    private String recoginzeName(Integer integer) {
        return switch (integer) {
            case 1 -> "SINGLE BED";
            case 2 -> "DOUBLE BED";
            case 3 -> "KING SIZE BED";
            case 4 -> "DOUBLE";
            case 5 -> "SOFA";
            case 6 -> "LARGE BED";
            case 7 -> "FUTON BED";
            default -> "SINGLE BED";
        };
    }

    private void saveFacility(String name, Hotel hotel) {
        for (FacilityEnum facilityName : FacilityEnum.values()) {
            if (facilityName.name().contains(name.toUpperCase()) || name.toUpperCase().contains(facilityName.name())) {
                if (facilityRepository.findByNameAndHotel_Id(facilityName, hotel.getId()).isEmpty()) {
                    Facility facility = Facility.builder()
                            .name(facilityName)
                            .hotel(hotel)
                            .currency(Currency.EUR)
                            .price(BigDecimal.valueOf(0))
                            .build();

                    facilityRepository.save(facility);
                    break;
                }
            }
        }
    }
}
