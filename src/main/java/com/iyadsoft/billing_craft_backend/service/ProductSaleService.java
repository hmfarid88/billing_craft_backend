package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.LossProfitAnalysis;
import com.iyadsoft.billing_craft_backend.dto.SalesItemDTO;
import com.iyadsoft.billing_craft_backend.dto.SalesRequest;
import com.iyadsoft.billing_craft_backend.entity.Customer;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;

import jakarta.transaction.Transactional;

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
            productSale.setSprice(salesItemDTO.getSprice());
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

    @Transactional
    public void deleteSaleAndCustomer(String username, String productno) {
        String cid = productSaleRepository.findCidByUsernameAndProductno(username, productno);
        if (cid == null) {
            throw new IllegalArgumentException("No sale found for this productno.");
        }

        long saleCount = productSaleRepository.countSalesByCustomerCid(cid);
        productSaleRepository.deleteByUsernameAndProductno(username, productno);

        if (saleCount == 1) {
            customerRepository.deleteByCid(cid);
        }
    }

    public String getLastCustomerCidByUsername(String username) {
        return productSaleRepository.findLastCustomerCidByUsername(username);
    }

    public List<Map<String, Object>> getLastSixMonthsSales(String username) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(6);

        List<Object[]> results = productSaleRepository.findLastSixMonthsSalesByUser(username, startDate, endDate);

        // Convert the results to a list of maps
        return results.stream().map(record -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", record[0]); // Month name
            map.put("totalSaleValue", record[1]); // Total sales value
            return map;
        }).toList();
    }


    // public List<Map<String, Object>> getMonthlyProfitLoss() {
    //     LocalDate endDate = LocalDate.now();
    //     LocalDate startDate = endDate.minusMonths(11);
    
    //     List<ProductSale> sales = productSaleRepository.findByDateBetween(startDate, endDate);
    
    //     return sales.stream()
    //             .collect(Collectors.groupingBy(
    //                     sale -> sale.getDate().getMonth().toString(),
    //                     Collectors.summarizingDouble(sale -> calculateProfitLoss(sale))
    //             ))
    //             .entrySet()
    //             .stream()
    //             .map(entry -> {
    //                 Map<String, Object> result = new HashMap<>();
    //                 result.put("month", entry.getKey());
    //                 result.put("Profit", entry.getValue().getSum() > 0 ? entry.getValue().getSum() : 0);
    //                 result.put("Loss", entry.getValue().getSum() < 0 ? Math.abs(entry.getValue().getSum()) : 0);
    //                 return result;
    //             })
    //             .collect(Collectors.toList());
    // }

    // private double calculateProfitLoss(ProductSale sale) {
    //     double profit = sale.getSprice() - sale.getProductStock().getPprice();
    //     double discount = sale.getDiscount() != null ? sale.getDiscount() : 0;
    //     return profit - discount;
    // }


    public List<LossProfitAnalysis> getLastTwelveMonthsProfitLoss(String username) {
        LocalDate startDate = LocalDate.now().minusMonths(12);
        return productSaleRepository.findLastTwelveMonthsProfitLoss(username, startDate);
    }
}
