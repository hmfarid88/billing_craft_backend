package com.iyadsoft.billing_craft_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.Customer;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository customerReository;

    CustomerController(CustomerRepository customerReository) {
        this.customerReository = customerReository;
    }

    @PostMapping("/saveCustomer")
    public Customer saveCustomer(@RequestBody Customer saveCustomer) {
        return customerReository.save(saveCustomer);
    }
}
