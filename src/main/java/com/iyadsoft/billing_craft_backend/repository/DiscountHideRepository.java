package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.DiscountHide;

public interface DiscountHideRepository extends JpaRepository<DiscountHide, Integer>{

    Optional<DiscountHide> findByUsername(String username);
    
}
