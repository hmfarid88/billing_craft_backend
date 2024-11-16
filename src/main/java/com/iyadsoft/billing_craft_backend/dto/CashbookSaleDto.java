package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashbookSaleDto {
    private LocalDate date;
    private String invoice;
    private Double value;

}
