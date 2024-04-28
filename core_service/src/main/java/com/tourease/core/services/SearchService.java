package com.tourease.core.services;

import com.tourease.core.models.custom.IndexVM;
import com.tourease.core.models.dto.DataSet;
import com.tourease.core.models.dto.FilterHotelListing;
import com.tourease.core.models.dto.HotelPreview;
import com.tourease.core.services.communication.HotelServiceClient;
import com.tourease.core.services.communication.TranslaterApiClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringTokenizer;

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

        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            filterHotelListing.setCountry(recognizeString(token, dataSet.countries()));
            filterHotelListing.setCity(recognizeString(token, dataSet.cities()));
            filterHotelListing.setAddress(recognizeString(token, dataSet.addresses()));
            filterHotelListing.setName(recognizeString(token, dataSet.names()));

        }


        return hotelServiceClient.getHotels(filterHotelListing);
    }

    private String recognizeString(String token, List<String> items) {
        for (String item : items) {
            if (item.toLowerCase().contains(token.toLowerCase())) {
                return item;
            }
        }
        return null;
    }
}
