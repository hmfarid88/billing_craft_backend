package com.iyadsoft.billing_craft_backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proId;
    private String username;
    private String category;
    private String brand;
    private String productName;
    private int pprice;
    private int sprice;
    private String color;
    private String supplier;
    private String supplierInvoice;
    private String productno;
    private String receiveDate;

   @OneToMany(mappedBy = "productStock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductSale> productSale;
}
