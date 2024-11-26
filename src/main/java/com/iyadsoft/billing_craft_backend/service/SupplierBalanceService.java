package com.iyadsoft.billing_craft_backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto;
import com.iyadsoft.billing_craft_backend.dto.SupplierSummaryDTO;
import com.iyadsoft.billing_craft_backend.repository.ProductSaleRepository;
import com.iyadsoft.billing_craft_backend.repository.ProductStockRepository;
import com.iyadsoft.billing_craft_backend.repository.SupplierPaymentRepository;

@Service
public class SupplierBalanceService {
    private final ProductStockRepository productStockRepository;
    private final ProductSaleRepository productSaleRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;

    @Autowired
    public SupplierBalanceService(ProductStockRepository productStockRepository, 
                                  ProductSaleRepository productSaleRepository, 
                                  SupplierPaymentRepository supplierPaymentRepository) {
        this.productStockRepository = productStockRepository;
        this.productSaleRepository = productSaleRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
    }

    public List<SupplierSummaryDTO> getSupplierData(String username) {
        // Fetch all distinct supplier names
        List<String> supplierNames = productStockRepository.findAllDistinctSupplierNames(username);

        List<SupplierSummaryDTO> summaries = new ArrayList<>();

        // Iterate over each supplier and aggregate the data
        for (String supplier : supplierNames) {
            Double totalProductValue = productStockRepository.findTotalProductValueByUsernameAndSupplier(username, supplier);
            Double totalSoldValue = productSaleRepository.findTotalSoldValueByUsernameAndSupplier(username, supplier);
            Double totalPayment = supplierPaymentRepository.findTotalPaymentByUsernameAndSupplier(username, supplier);
            Double totalReceive = supplierPaymentRepository.findTotalReceiveByUsernameAndSupplier(username, supplier);
            totalProductValue = (totalProductValue != null) ? totalProductValue : 0.0;
            totalSoldValue = (totalSoldValue != null) ? totalSoldValue : 0.0;
            totalPayment = (totalPayment != null) ? totalPayment : 0.0;
            totalReceive = (totalReceive != null) ? totalReceive : 0.0;
            Double balance = (totalProductValue + totalReceive) - (totalPayment + totalSoldValue);

            summaries.add(new SupplierSummaryDTO(
                    supplier,
                    totalProductValue != null ? totalProductValue : 0.0,
                    totalSoldValue != null ? totalSoldValue : 0.0,
                    totalPayment != null ? totalPayment : 0.0,
                    totalReceive != null ? totalReceive : 0.0,
                    balance));
        }

        return summaries;
    }

    public List<SupplierDetailsDto> getSupplierDetails(String username, String supplierName) {
        // Fetch data from each repository method
        List<SupplierDetailsDto> productPurchases = productStockRepository.findProductDetailsByUsernameAndSupplierName(username, supplierName);
        List<SupplierDetailsDto> productSales = productSaleRepository.findProductSalesByUsernameAndSupplierName(username, supplierName);
        List<SupplierDetailsDto> payments = supplierPaymentRepository.findDetailsPaymentByUsernameAndSupplier(username, supplierName);
        List<SupplierDetailsDto> receipts = supplierPaymentRepository.findDetailsReceiveByUsernameAndSupplier(username, supplierName);

        // Combine the results
        List<SupplierDetailsDto> combinedDetails = new ArrayList<>();
        combinedDetails.addAll(productPurchases);
        combinedDetails.addAll(productSales);
        combinedDetails.addAll(payments);
        combinedDetails.addAll(receipts);

        // Sort the combined list by date (assuming date is a field in SupplierDetailsDto)
        combinedDetails.sort(Comparator.comparing(SupplierDetailsDto::getDate));

        return combinedDetails;
    }
}
