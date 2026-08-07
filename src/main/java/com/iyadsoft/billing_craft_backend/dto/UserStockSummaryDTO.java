package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStockSummaryDTO {
    private String username;
    private Long stockQty;
    private Double stockValue;
}
