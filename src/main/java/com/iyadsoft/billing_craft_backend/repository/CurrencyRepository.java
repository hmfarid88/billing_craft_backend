package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
    Optional<Currency> findByUsername(String username);
    
}
