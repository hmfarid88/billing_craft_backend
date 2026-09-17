package com.iyadsoft.billing_craft_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.DataShow;
import com.iyadsoft.billing_craft_backend.repository.DataShowRepository;

@Service
public class DataShowService {
    @Autowired
    private DataShowRepository dataShowRepository;

    public DataShow saveOrUpdateVat(DataShow newData) {
        Optional<DataShow> existingData = dataShowRepository.findByUsername(newData.getUsername());
        if (existingData.isPresent()) {
            DataShow dataShow = existingData.get();
            dataShow.setPercent(newData.getPercent());
            return dataShowRepository.save(dataShow);
        } else {
            return dataShowRepository.save(newData);
        }

    }

    public Optional<Double> getPercentByUsername(String username) {
        return dataShowRepository.findPercentByUsername(username);
    }
}
