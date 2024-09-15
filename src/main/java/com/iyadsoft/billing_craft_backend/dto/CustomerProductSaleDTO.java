package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProductSaleDTO {
    private String cName;
    private String phoneNumber;
    private String address;
    private String productName;
    private String productno;
    private String color;
    private Integer sprice;
    private Integer discount;
    private Integer offer;
    private LocalDate saleDate;
    private String cid;
    private Long proId;
    private String username;

}
