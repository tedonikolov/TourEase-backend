package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Owner;

import java.util.Set;

public record OwnerInfo(Long id,
                        String email,
                        String fullName,
                        String companyName,
                        String companyAddress,
                        String phone,
                        String country,
                        String city,
                        String eik,
                        Set<Hotel> hotels) {
    public OwnerInfo(Owner owner){
        this(owner.getId(), owner.getEmail(), owner.getFullName(), owner.getCompanyName(), owner.getCompanyAddress(), owner.getPhone(), owner.getCountry(), owner.getCity(), owner.getEik(), owner.getHotels());
    }
}
