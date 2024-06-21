package com.iyadsoft.billing_craft_backend.dto;

import java.sql.Timestamp;

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
    private String brand;
    private String productName;
    private String productno;
    private String color;
    private Timestamp saleDate;
    private int sprice;
    private int discount;
    private int offer;
    private int cardAmount;
    private int vatAmount;
    private int dueAmount;
    private String cid;
}
