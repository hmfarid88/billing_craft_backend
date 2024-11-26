package com.iyadsoft.billing_craft_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.Vat;
import com.iyadsoft.billing_craft_backend.repository.VatRepository;


@Service
public class VatService {
    @Autowired
    private VatRepository vatRepository;

    public Vat saveOrUpdateVat(Vat newVat) {
        Optional<Vat> existingVat = vatRepository.findByUsername(newVat.getUsername());
        if (existingVat.isPresent()) {
            Vat vat = existingVat.get();
            vat.setPercent(newVat.getPercent());
            return vatRepository.save(vat);
        } else {
            return vatRepository.save(newVat);
        }

    }

    public Optional<Double> getPercentByUsername(String username) {
        return vatRepository.findPercentByUsername(username);
    }
}
