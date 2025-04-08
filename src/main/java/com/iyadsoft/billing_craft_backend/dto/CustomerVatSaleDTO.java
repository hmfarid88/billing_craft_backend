package com.iyadsoft.billing_craft_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVatSaleDTO {
    String cid;
    String cName;
    String phoneNumber;
    Double vatAmount;
    LocalDate date;
}
