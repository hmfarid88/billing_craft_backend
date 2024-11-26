package com.iyadsoft.billing_craft_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRecevBalance {
    private String paymentName;
    private Double payment;
    private Double receive;
}
