package com.iyadsoft.billing_craft_backend.service;

import java.time.LocalDate;
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
            Double totalProductValue = productStockRepository.findTotalProductValueByUsernameAndSupplier(username,
                    supplier);
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
   
    private Double calculateOpeningBalance(
            String username,
            String supplierName,
            LocalDate fromDate) {

        Double purchase = productStockRepository.sumPurchaseBeforeDate(
                username, supplierName, fromDate);

        Double sale = productSaleRepository.sumVendorSaleBeforeDate(
                username, supplierName, fromDate);

        Double returned = productStockRepository.sumReturnedBeforeDate(
                username, supplierName, fromDate);

        Double payment = supplierPaymentRepository.sumPaymentBeforeDate(
                username, supplierName, fromDate);

        Double receive = supplierPaymentRepository.sumReceiveBeforeDate(
                username, supplierName, fromDate);
        return value(purchase)
                - value(sale)
                - value(returned)
                - value(payment)
                + value(receive);

    }

    public List<SupplierDetailsDto> getSupplierDetails(
            String username,
            String supplierName,
            LocalDate fromDate,
            LocalDate toDate) {

        // Opening balance
        Double openingBalance = calculateOpeningBalance(username, supplierName, fromDate);

        // Current period transactions
        List<SupplierDetailsDto> purchases = productStockRepository
                .findProductDetailsByUsernameAndSupplierName(username, supplierName, fromDate, toDate);

        List<SupplierDetailsDto> returns = productStockRepository.findReturnedDetailsByUsernameAndSupplierName(username,
                supplierName, fromDate, toDate);

        List<SupplierDetailsDto> sales = productSaleRepository.findProductSalesByUsernameAndSupplierName(username,
                supplierName, fromDate, toDate);

        List<SupplierDetailsDto> payments = supplierPaymentRepository.findDetailsPaymentByUsernameAndSupplier(username,
                supplierName, fromDate, toDate);

        List<SupplierDetailsDto> receives = supplierPaymentRepository.findDetailsReceiveByUsernameAndSupplier(username,
                supplierName, fromDate, toDate);

        List<SupplierDetailsDto> list = new ArrayList<>();

        list.addAll(purchases);
        list.addAll(returns);
        list.addAll(sales);
        list.addAll(payments);
        list.addAll(receives);

        list.sort(Comparator
                .comparing(SupplierDetailsDto::getDate)
                .thenComparing(SupplierDetailsDto::getInvoice,
                        Comparator.nullsLast(String::compareTo)));

        double balance = openingBalance;

        for (SupplierDetailsDto dto : list) {

            dto.setOpeningBalance(balance);

            balance += value(dto.getPvalue())
                    - value(dto.getSvalue())
                    - value(dto.getRvalue())
                    - value(dto.getPayment())
                    + value(dto.getReceive());

            dto.setRunningBalance(balance);
        }

        return list;
    }

    private double value(Double value) {
        return value == null ? 0 : value;
    }
}
