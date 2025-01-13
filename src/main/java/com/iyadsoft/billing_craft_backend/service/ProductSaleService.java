package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.LossProfitAnalysis;
import com.iyadsoft.billing_craft_backend.dto.SalesItemDTO;
import com.iyadsoft.billing_craft_backend.dto.SalesRequest;
import com.iyadsoft.billing_craft_backend.dto.SixMonthAnalysis;
import com.iyadsoft.billing_craft_backend.entity.Customer;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.repository.CustomerRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductSaleService {
    private final SmsService smsService;
    private final CustomerRepository customerRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ProductStockRepository productStockRepository;

    @Autowired
    public ProductSaleService(SmsService smsService, CustomerRepository customerRepository,
            ProductSaleRepository productSaleRepository,
            ProductStockRepository productStockRepository) {
        this.smsService = smsService;
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
        double totalValue = 0;
        // Loop through each sales item in the request
        for (SalesItemDTO salesItemDTO : saleRequest.getSalesItems()) {
            // Fetch ProductStock based on proId
            ProductStock productStock = productStockRepository.findById(salesItemDTO.getProId())
                    .orElseThrow(
                            () -> new RuntimeException("ProductStock not found for proId: " + salesItemDTO.getProId()));

            totalValue += (salesItemDTO.getSprice() - salesItemDTO.getDiscount() - salesItemDTO.getOffer());
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

        String smsResponse = smsService.sendSms(
                savedCustomer.getUsername(),
                savedCustomer.getPhoneNumber(),
                "Dear " + savedCustomer.getCName() + ", your total bill is ৳" + totalValue + ". Thank you for shopping from " + savedCustomer.getUsername() + " !");

        System.out.println("SMS API Response: " + smsResponse);

        return null;

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

        // Fetch data from repository
        List<SixMonthAnalysis> results = productSaleRepository.findLastSixMonthsSalesByUser(username, startDate,
                endDate);

        // Convert the results to a list of maps
        return results.stream().map(record -> {
            Map<String, Object> map = new HashMap<>();
            map.put("month", record.getMonth()); // Use getter for the month
            map.put("Value", record.getValue()); // Use getter for the total sales value
            return map;
        }).toList();
    }

    public List<LossProfitAnalysis> getLastTwelveMonthsProfitLoss(String username) {
        LocalDate startDate = LocalDate.now().minusMonths(12);
        return productSaleRepository.findLastTwelveMonthsProfitLoss(username, startDate);
    }
}
