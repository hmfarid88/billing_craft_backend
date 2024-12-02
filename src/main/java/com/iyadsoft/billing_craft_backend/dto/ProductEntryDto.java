package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntryDto {
    private String category;
    private String brand;
    private String productName;
    private Double pprice;
    private Double sprice;
    private String color;
    private String supplier;
    private String supplierInvoice;
    private String productno;
    private LocalDate date;
    private LocalTime time;
}
