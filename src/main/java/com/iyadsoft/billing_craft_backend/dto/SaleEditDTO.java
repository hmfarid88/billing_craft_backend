package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleEditDTO {
    private Long saleId;

    private String productno;

    private String productName;

    private LocalDate date;

    private LocalTime time;

    private String saleType;

    private Double sprice;

    private Double discount;

    private Double offer;
}
