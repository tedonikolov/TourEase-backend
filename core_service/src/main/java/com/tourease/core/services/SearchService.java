package com.tourease.core.services;

import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.DataSet;
import com.tourease.core.models.dto.FilterHotelListing;
import com.tourease.core.models.dto.HotelPreview;
import com.tourease.core.models.enums.Facility;
import com.tourease.core.models.enums.MealType;
import com.tourease.core.models.enums.Stars;
import com.tourease.core.services.communication.HotelServiceClient;
import com.tourease.core.services.communication.TranslaterApiClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@AllArgsConstructor
public class SearchService {
    private final HotelServiceClient hotelServiceClient;
    private final TranslaterApiClient translaterApiClient;

    public IndexVM<HotelPreview> listing(String searchText, int page) {
        hotelServiceClient.checkConnection();
        String text = translaterApiClient.translate(searchText);

        DataSet dataSet = hotelServiceClient.getDataSet();

        FilterHotelListing filterHotelListing = new FilterHotelListing();
        filterHotelListing.setPageNumber(page);
        List<Facility> facilities = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(text);

        String token = tokenizer.nextToken();
        while (true) {
            if(token.equals("star") || token.equals("stars")) {
                if (!tokenizer.hasMoreTokens()) {
                    break;
                }
                token = tokenizer.nextToken();
                continue;
            }

            if(!Objects.equals(token, "up") && !Objects.equals(token, "to") && !Objects.equals(token, "from") && !Objects.equals(token, "between")) {
                if(recognizeToken(token, dataSet.countries())!=null)
                    filterHotelListing.setCountry(token);

                if(containsToken(token, dataSet.cities())!=null)
                    filterHotelListing.setCity(containsToken(token, dataSet.cities()));
            }

            if(!token.equalsIgnoreCase("breakfast"))
                recognizeFacility(token, Facility.values(), facilities);

            filterHotelListing.setMealType(recognizeMeal(token, MealType.values()));

            if (!tokenizer.hasMoreTokens()) {
                break;
            }

            String nextToken = tokenizer.nextToken();

            if(Objects.equals(token, "hotel") && containsToken(nextToken, dataSet.names()) != null)
                filterHotelListing.setName(nextToken);

            if(Objects.equals(nextToken, "people") || Objects.equals(nextToken, "person")) {
                filterHotelListing.setPeople(Integer.parseInt(token));
            }

            if(Objects.equals(nextToken, "stars") || Objects.equals(nextToken, "star")) {
                filterHotelListing.setStars(recognizeStars(token));
            }

            String regex = "[1-5]-\\bstar\\b|[1-5]-\\bstars\\b";
            if(token.matches(regex)){
                filterHotelListing.setStars(recognizeStars(token.substring(0, 1)));
            }

            if(Objects.equals(token, "from")) {
                LocalDate date = recognizeDate(nextToken);
                if (date != null) {
                    LocalDate toDate = recognizeDate(nextToken);
                    if(toDate == null) {
                        nextToken = tokenizer.nextToken();
                    }
                    filterHotelListing.setFromDate(recognizeDate(nextToken));
                } else {
                    try {
                        filterHotelListing.setFromPrice(new BigDecimal(nextToken));
                    } catch (NumberFormatException e) {
                        nextToken = tokenizer.nextToken();
                        filterHotelListing.setFromPrice(new BigDecimal(nextToken));
                    }
                }
            }

            if(Objects.equals(token, "to") || Objects.equals(token, "until")) {
                LocalDate date = recognizeDate(nextToken);
                if (date != null) {
                    LocalDate toDate = recognizeDate(nextToken);
                    if(toDate == null) {
                        nextToken = tokenizer.nextToken();
                    }
                    filterHotelListing.setToDate(recognizeDate(nextToken));
                } else {
                    try {
                        filterHotelListing.setToPrice(new BigDecimal(nextToken));
                    } catch (NumberFormatException e) {
                        nextToken = tokenizer.nextToken();
                        filterHotelListing.setToPrice(new BigDecimal(nextToken));
                    }
                }
            }

            if(Objects.equals(token, "between")) {
                LocalDate date = recognizeDate(nextToken);
                if (date != null) {
                    filterHotelListing.setFromDate(date);
                    nextToken = tokenizer.nextToken();
                    LocalDate toDate = recognizeDate(nextToken);
                    if(toDate == null) {
                        nextToken = tokenizer.nextToken();
                    }
                    filterHotelListing.setToDate(recognizeDate(nextToken));
                } else {
                    filterHotelListing.setFromPrice(new BigDecimal(nextToken));
                    nextToken = tokenizer.nextToken();
                    try {
                        filterHotelListing.setToPrice(new BigDecimal(nextToken));
                    } catch (NumberFormatException e) {
                        nextToken = tokenizer.nextToken();
                        filterHotelListing.setToPrice(new BigDecimal(nextToken));
                    }
                }
            }

            token = nextToken;
        }
        filterHotelListing.setFacilities(facilities);

        if (filterHotelListing.getCountry() == null && filterHotelListing.getCity() == null && filterHotelListing.getName() == null && filterHotelListing.getStars() == null && filterHotelListing.getFacilities().isEmpty() && filterHotelListing.getPeople() == null && filterHotelListing.getFromPrice() == null && filterHotelListing.getToPrice() == null && filterHotelListing.getFromDate() == null && filterHotelListing.getToDate() == null && filterHotelListing.getMealType() == null)
            return null;

        return hotelServiceClient.getHotels(filterHotelListing);
    }

    private String containsToken(String token, List<String> items) {
        for (String item : items) {
            if (item.toLowerCase().contains(token.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    private String recognizeToken(String token, List<String> items) {
        for (String item : items) {
            if (item.equalsIgnoreCase(token.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    private void recognizeFacility(String token, Facility[] items, List<Facility> facilities) {
        for (Facility item : items) {
            if (item.name().equalsIgnoreCase(token.toLowerCase())) {
                facilities.add(item);
            }
        }
    }

    private MealType recognizeMeal(String token, MealType[] items) {
        for (MealType item : items) {
            if (item.name().equalsIgnoreCase(token.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    private Stars recognizeStars(String token) {
        return switch (token) {
            case "1" -> Stars.ONE;
            case "2" -> Stars.TWO;
            case "3" -> Stars.THREE;
            case "4" -> Stars.FOUR;
            case "5" -> Stars.FIVE;
            default -> null;
        };
    }

    public LocalDate recognizeDate(String dateString) {
        List<String> formatStrings = Arrays.asList("yyyy-MM-dd", "dd-MM-yyyy", "MM-dd-yyyy", "yyyy/MM/dd", "dd/MM/yyyy", "MM/dd/yyyy");

        for (String formatString : formatStrings) {
            try {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(formatString));
            } catch (DateTimeParseException ignored) {
            }
        }

        return null;
    }
}
