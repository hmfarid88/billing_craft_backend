package com.iyadsoft.billing_craft_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.Currency;
import com.iyadsoft.billing_craft_backend.repository.CurrencyRepository;

@Service
public class CurrencyService {
     @Autowired
    private CurrencyRepository currencyRepository;

    public Currency saveOrUpdateCurrency(String username, String currencyValue) {
        return currencyRepository.findByUsername(username)
                .map(existingCurrency -> {
                    existingCurrency.setCurrency(currencyValue);
                    return currencyRepository.save(existingCurrency);
                })
                .orElseGet(() -> {
                    Currency newCurrency = new Currency();
                    newCurrency.setUsername(username);
                    newCurrency.setCurrency(currencyValue);
                    return currencyRepository.save(newCurrency);
                });
    }

    public Currency getCurrencyByUsername(String username) {
        Optional<Currency> optionalCurrency = currencyRepository.findByUsername(username);
        return optionalCurrency.orElseThrow(() -> 
                new RuntimeException("Currency not found for username: " + username));
    }
}
