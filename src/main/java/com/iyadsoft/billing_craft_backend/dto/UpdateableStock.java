package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateableStock {
    private String supplier;
    private String productName;
    private Double pprice;
    private Long qty;
}
