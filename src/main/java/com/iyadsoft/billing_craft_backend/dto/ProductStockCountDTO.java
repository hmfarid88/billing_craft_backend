package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockCountDTO {
    private String category;
    private String brand;
    private String productName;
    private Long countBeforeToday;
    private Long countToday;
}
