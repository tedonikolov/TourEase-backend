package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.OwnerSaveVO;
import com.tourease.hotel.models.entities.Owner;

public class OwnerMapper {
    public static void updateEntity(Owner owner, OwnerSaveVO newOwner){
        owner.setFullName(newOwner.fullName());
        owner.setCompanyName(newOwner.companyName());
        owner.setCountry(newOwner.country());
        owner.setCity(newOwner.city());
        owner.setCompanyAddress(newOwner.companyAddress());
        owner.setPhone(newOwner.phone());
        owner.setEik(newOwner.eik());
    }
}
