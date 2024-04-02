package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.models.mappers.CustomerMapper;
import com.tourease.hotel.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerInfo) {
        Customer customer = CustomerMapper.toEntity(customerInfo);

        return customerRepository.save(customer);
    }

    public CustomerDTO getCustomerByPassportId(String passportId) {
        Customer customer = findByPassportId(passportId);

        if (customer == null) {
            throw new CustomException("Customer not found", ErrorCode.EntityNotFound);
        }

        return CustomerMapper.toModel(customer);
    }

    public Customer findByPassportId(String passportId) {
        return customerRepository.findByPassportId(passportId).orElse(null);
    }
}