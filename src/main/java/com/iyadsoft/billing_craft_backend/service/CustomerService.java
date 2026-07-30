package com.iyadsoft.billing_craft_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.CustomerDto;
import com.iyadsoft.billing_craft_backend.dto.CustomerUpdateDto;
import com.iyadsoft.billing_craft_backend.entity.Customer;
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

     public ResponseEntity<?> updateCustomer(String username, CustomerUpdateDto dto) {

        Optional<Customer> optionalCustomer =
                customerRepository.findByUsernameAndCid(username, dto.getCid());

        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        customer.setCName(dto.getCName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());

        customerRepository.save(customer);

        return ResponseEntity.ok("Customer updated successfully");
    }
}
