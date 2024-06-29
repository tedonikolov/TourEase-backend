package com.tourease.hotel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.HashSet;

import com.tourease.hotel.models.dto.requests.CustomerDTO;
import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.repositories.CustomerRepository;
import com.tourease.hotel.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setPassportId("P123456");
        customer.setFullName("Ivan Dimov");
        customer.setBirthDate(LocalDate.of(1990, 5, 12));
        customer.setCountry("Bulgaria");
        customer.setGender("Male");
        customer.setPhoneNumber("1234567890");
        customer.setEmail("iv.dimov@example.com");
        customer.setCreationDate(LocalDate.now());
        customer.setExpirationDate(LocalDate.now().plusYears(10));
        customer.setReservations(new HashSet<>());

        customerDTO = new CustomerDTO(
                1L,
                "Ivan Dimov",
                "1234567890",
                "iv.dimoc@example.com",
                "P123456",
                LocalDate.of(1990, 5, 12),
                LocalDate.now(),
                LocalDate.now().plusYears(10),
                "Bulgaria",
                "Male"
        );
    }

    @Test
    public void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customerDTO);

        assertNotNull(createdCustomer);
        assertEquals("Ivan Dimov", createdCustomer.getFullName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testGetCustomerByPassportId() {
        when(customerRepository.findByPassportId("P123456")).thenReturn(Optional.of(customer));

        CustomerDTO foundCustomerDTO = customerService.getCustomerByPassportId("P123456");

        assertNotNull(foundCustomerDTO);
        assertEquals("Ivan Dimov", foundCustomerDTO.fullName());
        verify(customerRepository, times(1)).findByPassportId("P123456");
    }

    @Test
    public void testGetCustomerByPassportIdNotFound() {
        when(customerRepository.findByPassportId("P123456")).thenReturn(Optional.empty());

        CustomerDTO foundCustomerDTO = customerService.getCustomerByPassportId("P123456");

        assertNull(foundCustomerDTO);
        verify(customerRepository, times(1)).findByPassportId("P123456");
    }

    @Test
    public void testFindByPassportId() {
        when(customerRepository.findByPassportId("P123456")).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findByPassportId("P123456");

        assertNotNull(foundCustomer);
        assertEquals("Ivan Dimov", foundCustomer.getFullName());
        verify(customerRepository, times(1)).findByPassportId("P123456");
    }

    @Test
    public void testFindByPassportIdNotFound() {
        when(customerRepository.findByPassportId("P123456")).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.findByPassportId("P123456");

        assertNull(foundCustomer);
        verify(customerRepository, times(1)).findByPassportId("P123456");
    }

    @Test
    public void testUpdateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.updateCustomer(customer, customerDTO);

        assertEquals("Ivan Dimov", customer.getFullName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testFindById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findById(1L);

        assertNotNull(foundCustomer);
        assertEquals("Ivan Dimov", foundCustomer.getFullName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.findById(1L);

        assertNull(foundCustomer);
        verify(customerRepository, times(1)).findById(1L);
    }
}
