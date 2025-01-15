package com.iyadsoft.billing_craft_backend.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.dto.LossProfitAnalysis;
import com.iyadsoft.billing_craft_backend.dto.SalesRequest;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;
import com.iyadsoft.billing_craft_backend.service.ProductSaleService;

@RestController
@RequestMapping("/sales")
public class ProductSaleController {
    private final ProductSaleService productSaleService;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private ProductSaleRepository productSaleRepository;

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
            response.put("customer", saleRequest.getCustomer());
            response.put("salesItems", savedSalesItems);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An error occurred while processing the sale"));
        }
    }

    @PostMapping("/purchaseReturn")
    public ProductSale purchaseReturn(@RequestParam Long proId, @RequestParam String username) {
                     
        ProductStock productStock = productStockRepository.findById(proId)
                .orElseThrow(() -> new RuntimeException("ProductStock not found"));
        ZonedDateTime dhakaTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));
        ProductSale productSale = new ProductSale();
        productSale.setSaleType("returned");
        productSale.setSprice(0.0);
        productSale.setDiscount(0.0);
        productSale.setOffer(0.0);
        productSale.setDate(LocalDate.now());
        productSale.setTime(dhakaTime.toLocalTime());
        productSale.setUsername(username);
        productSale.setProductStock(productStock);

        return productSaleRepository.save(productSale);

    }

    @DeleteMapping("/saleReturn")
    public ResponseEntity<Object> deleteSaleAndCustomer(@RequestParam String username, @RequestParam String productno) {
        try {
            productSaleService.deleteSaleAndCustomer(username, productno);
            return ResponseEntity.ok(Map.of("message", "Sale deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred."));
        }
    }

    @GetMapping("/lastCustomerCid")
    public ResponseEntity<?> getLastCustomerCid(@RequestParam String username) {
        String lastCid = productSaleService.getLastCustomerCidByUsername(username);
        if (lastCid != null) {
            Map<String, String> response = new HashMap<>();
            response.put("lastCid", lastCid);
            return ResponseEntity.ok(response); // This ensures a JSON response
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer CID not found");
        }
    }

    @GetMapping("/last-six-months")
    public List<Map<String, Object>> getLastSixMonthsSales(@RequestParam String username) {
        return productSaleService.getLastSixMonthsSales(username);
    }

    @GetMapping("/last-12-months")
    public ResponseEntity<List<LossProfitAnalysis>> getLastTwelveMonthsProfitLoss(@RequestParam String username) {
        List<LossProfitAnalysis> profitLossData = productSaleService.getLastTwelveMonthsProfitLoss(username);
        return ResponseEntity.ok(profitLossData);
    }
}
