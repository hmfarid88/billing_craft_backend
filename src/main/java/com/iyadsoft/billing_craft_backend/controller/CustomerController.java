package com.iyadsoft.billing_craft_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.dto.CustomerDto;
import com.iyadsoft.billing_craft_backend.entity.Customer;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;
import com.iyadsoft.billing_craft_backend.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository customerReository;
    private final CustomerService customerService;

    CustomerController(CustomerRepository customerReository, CustomerService customerService) {
        this.customerReository = customerReository;
        this.customerService = customerService;
    }

    @PostMapping("/saveCustomer")
    public Customer saveCustomer(@RequestBody Customer saveCustomer) {
        return customerReository.save(saveCustomer);
    }

    @GetMapping("/customers")
    public List<CustomerDto> getCustomers(@RequestParam String username, @RequestParam String phoneNumber) {
        return customerService.getCustomersByUsernameAndPhoneNumber(username, phoneNumber);
    }
}
