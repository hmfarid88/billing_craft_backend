package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierSummaryDTO {
    private String supplierName;
    private Double totalProductValue;
    private Double totalSoldValue;
    private Double totalPayment;
    private Double totalReceive;
    private Double balance;
}
