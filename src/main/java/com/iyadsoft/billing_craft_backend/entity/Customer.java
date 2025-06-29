package com.iyadsoft.billing_craft_backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer", indexes = {
    @Index(name = "idx_customer_cid", columnList = "cid"),
    @Index(name = "idx_customer_username", columnList = "username"),
    @Index(name = "idx_customer_soldby", columnList = "soldby")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    private String cid;
    private String cName;
    private String phoneNumber;
    private String address;
    private String soldby;
    private Double cardPay;
    private Double vatAmount;
    private Double received;
    private String username;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductSale> productSale;
    
}
