package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaleReturnDto {
    private String category;
    private String brand;
    private String productName;
    private String color;
    private String productno;
    private String supplier;
    private String supplierInvoice;
    private Double pprice;
    private Double sprice;
    private LocalDate date;
    private LocalTime time;

}
