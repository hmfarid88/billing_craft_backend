package com.iyadsoft.billing_craft_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO;
import com.iyadsoft.billing_craft_backend.entity.Customer;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductSaleService {
    @Autowired
    private ProductSaleRepository productSaleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Transactional
    public ProductSale createProductSale(CustomerProductSaleDTO productSaleDTO) {
        Customer customer = customerRepository.findById(productSaleDTO.getCid())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        ProductStock productStock = productStockRepository.findById(productSaleDTO.getProId())
                .orElseThrow(() -> new RuntimeException("ProductStock not found"));

        ProductSale productSale = new ProductSale();
        productSale.setCustomer(customer);
        productSale.setProductStock(productStock);
        productSale.setDiscount(productSaleDTO.getDiscount());
        productSale.setOffer(productSaleDTO.getOffer());
        productSale.setSaleDate(productSaleDTO.getSaleDate());
        productSale.setUsername(productSaleDTO.getUsername());

        return productSaleRepository.save(productSale);
    }
}
