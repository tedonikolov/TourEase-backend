package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.models.mappers.CustomerMapper;
import com.tourease.hotel.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerInfo) {
        Customer customer = CustomerMapper.toEntity(customerInfo);
        customer.setReservations(new HashSet<>());

        return customerRepository.save(customer);
    }

    public CustomerDTO getCustomerByPassportId(String passportId) {
        Customer customer = findByPassportId(passportId);

        if (customer == null) {
            return null;
        }

        return CustomerMapper.toModel(customer);
    }

    public Customer findByPassportId(String passportId) {
        return customerRepository.findByPassportId(passportId).orElse(null);
    }

    public void updateCustomer(Customer customer, CustomerDTO customerDTO) {
        CustomerMapper.updateEntity(customer, customerDTO);
        customerRepository.save(customer);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}