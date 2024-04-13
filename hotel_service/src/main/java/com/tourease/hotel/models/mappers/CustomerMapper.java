package com.tourease.hotel.models.mappers;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.entities.Customer;

public class CustomerMapper {
    public static Customer toEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.id())
                .fullName(customerDTO.fullName())
                .phoneNumber(customerDTO.phoneNumber())
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
                customer.getId(),
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

    public static void updateEntity(Customer customer, CustomerDTO customerDTO) {
        customer.setId(customerDTO.id());
        customer.setFullName(customerDTO.fullName());
        customer.setPhoneNumber(customerDTO.phoneNumber());
        customer.setPassportId(customerDTO.passportId());
        customer.setBirthDate(customerDTO.birthDate());
        customer.setCreationDate(customerDTO.creationDate());
        customer.setExpirationDate(customerDTO.expirationDate());
        customer.setCountry(customerDTO.country());
        customer.setGender(customerDTO.gender());
    }
}