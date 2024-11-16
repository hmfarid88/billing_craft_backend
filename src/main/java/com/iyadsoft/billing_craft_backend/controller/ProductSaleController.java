package com.iyadsoft.billing_craft_backend.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.dto.SalesRequest;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;
import com.iyadsoft.billing_craft_backend.service.ProductSaleService;

@RestController
@RequestMapping("/sales")
public class ProductSaleController {
    private final ProductSaleService productSaleService;

    @Autowired
    public ProductSaleController(ProductSaleService productSaleService) {
        this.productSaleService = productSaleService;
    }

    @PostMapping("/productSale")
    public ResponseEntity<?> handleSale(@RequestBody SalesRequest saleRequest) {
        try {
            // Process the sales using the service
            List<ProductSale> savedSalesItems = productSaleService.processSales(saleRequest);

            // Prepare the response
            Map<String, Object> response = new HashMap<>();
            response.put("customer", saleRequest.getCustomer()); // Assuming customer info is directly from the request
            response.put("salesItems", savedSalesItems);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An error occurred while processing the sale"));
        }
    }
}
