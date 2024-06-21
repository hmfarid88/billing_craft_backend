package com.iyadsoft.billing_craft_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

}
