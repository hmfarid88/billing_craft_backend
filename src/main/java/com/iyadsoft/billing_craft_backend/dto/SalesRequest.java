package com.iyadsoft.billing_craft_backend.dto;

import java.util.List;

import com.iyadsoft.billing_craft_backend.entity.Customer;

import lombok.Data;
@Data
public class SalesRequest {
    private List<SalesItemDTO> salesItems;
    private Customer customer;
}
