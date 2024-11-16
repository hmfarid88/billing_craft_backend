package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;

@Service
public class ProductStockService {
    @Autowired
    private ProductStockRepository productStockRepository;

    public List<ProductStockCountDTO> getProductCountByUserAndGroup(String username) {
        LocalDate today = LocalDate.now();
        return productStockRepository.countProductByUsernameGroupByCategoryBrandProductName(username, today);
    }
}
