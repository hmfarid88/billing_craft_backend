package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitItemDto {
    private String category;
    private String brand;
    private String productName;
    private Long qty;
    private Double pprice;
    private Double sprice;
    private Double discount;
}
