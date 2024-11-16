package com.iyadsoft.billing_craft_backend.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.SalesItemDTO;
import com.iyadsoft.billing_craft_backend.dto.SalesRequest;
import com.iyadsoft.billing_craft_backend.entity.Customer;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;

@Service
public class ProductSaleService {
    private final CustomerRepository customerRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ProductStockRepository productStockRepository;

    @Autowired
    public ProductSaleService(CustomerRepository customerRepository,
            ProductSaleRepository productSaleRepository,
            ProductStockRepository productStockRepository) {
        this.customerRepository = customerRepository;
        this.productSaleRepository = productSaleRepository;
        this.productStockRepository = productStockRepository;
    }

    public List<ProductSale> processSales(SalesRequest saleRequest) {
        // Save or retrieve the customer
        Customer customer = saleRequest.getCustomer();
        Customer savedCustomer = customerRepository.save(customer);

        // Prepare to save each ProductSale item
        List<ProductSale> savedSalesItems = new ArrayList<>();
        ZonedDateTime dhakaTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));

        // Loop through each sales item in the request
        for (SalesItemDTO salesItemDTO : saleRequest.getSalesItems()) {
            // Fetch ProductStock based on proId
            ProductStock productStock = productStockRepository.findById(salesItemDTO.getProId())
                    .orElseThrow(
                            () -> new RuntimeException("ProductStock not found for proId: " + salesItemDTO.getProId()));

            // Create ProductSale and set fields
            ProductSale productSale = new ProductSale();
            productSale.setCustomer(savedCustomer); // Associate with saved customer
            productSale.setProductStock(productStock); // Associate with product stock
            productSale.setSaleType(salesItemDTO.getSaleType());
            productSale.setDiscount(salesItemDTO.getDiscount());
            productSale.setOffer(salesItemDTO.getOffer());
            productSale.setDate(salesItemDTO.getDate());
            productSale.setTime(dhakaTime.toLocalTime()); // Set local Dhaka time
            productSale.setUsername(salesItemDTO.getUsername());

            // Save each ProductSale item
            savedSalesItems.add(productSaleRepository.save(productSale));
        }

        return savedSalesItems;
    }
}
