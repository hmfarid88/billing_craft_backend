package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private String category;
    private String brand;
    private String productName;
    private String productno;
    private String color;
    private Double pprice;
    private Double sprice;
    private String supplier;
    private String supplierInvoice;
    private LocalDate entryDate;
    private LocalTime entryTime;
    private String status;
    private String saleType;
    private Double soldprice;
    private Double discount;
    private Double offer;
    private LocalDate saleDate;
    private LocalTime saleTime;
    private String invoice;
    private String cName;
    private String phoneNumber;
    private String soldby;
   

}
