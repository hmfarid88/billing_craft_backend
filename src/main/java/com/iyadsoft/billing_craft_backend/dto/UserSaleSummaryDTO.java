package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSaleSummaryDTO {
    private String username;
    private Long saleQty;
    private Double saleValue;
    private Double purchaseValue;
    private Double profit;
}
