package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class SalesItemDTO {
    private Long proId;
    private Double sprice;
    private Double discount;
    private Double offer;
    private LocalDate date;
    private String saleType;
    private String username;
}
