package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LossProfitAnalysis {
    private String month; 
    private Double profit; 
    private Double loss;
}
