package com.iyadsoft.billing_craft_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.CustomerDto;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDto> getCustomersByUsernameAndPhoneNumber(String username, String phoneNumber) {
        return customerRepository.findByUsernameAndPhoneNumber(username, phoneNumber);
    }
}
