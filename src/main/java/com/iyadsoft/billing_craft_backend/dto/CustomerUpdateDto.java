package com.iyadsoft.billing_craft_backend.dto;

import lombok.Data;

@Data
public class CustomerUpdateDto {
    private String cid;
    private String cName;
    private String phoneNumber;
    private String address;
}
