package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.entities.Customer;

public class CustomerMapper {
    public static Customer toEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .fullName(customerDTO.fullName())
                .phoneNumber(customerDTO.phone())
                .passportId(customerDTO.passportId())
                .birthDate(customerDTO.birthDate())
                .creationDate(customerDTO.creationDate())
                .expirationDate(customerDTO.expirationDate())
                .country(customerDTO.country())
                .gender(customerDTO.gender())
                .build();
    }

    public static CustomerDTO toModel(Customer customer) {
        return new CustomerDTO(
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getPassportId(),
                customer.getBirthDate(),
                customer.getCreationDate(),
                customer.getExpirationDate(),
                customer.getCountry(),
                customer.getGender()
        );
    }
}