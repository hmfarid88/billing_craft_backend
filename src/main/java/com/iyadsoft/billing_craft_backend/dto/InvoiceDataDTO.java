package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDataDTO {
    private String cname;
    private String phoneNumber;
    private String address;
    private String soldby;
    private String brand;
    private String productName;
    private String productno;
    private String color;
    private LocalDate date;
    private LocalTime time;
    private String saleType;
    private Double pprice;
    private Double sprice;
    private Double discount;
    private Double offer;
    private Double cardAmount;
    private Double vatAmount;
    private Double received;
    private String cid;
    private Long saleId;
}
