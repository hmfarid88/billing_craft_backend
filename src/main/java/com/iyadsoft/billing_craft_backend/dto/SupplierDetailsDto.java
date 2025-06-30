package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDetailsDto {
    private LocalDate date;
    private String invoice;
    private Long qty;
    private Double pvalue;
    private Double svalue;
    private Double payment;
    private Double receive;
    private String note;
}
