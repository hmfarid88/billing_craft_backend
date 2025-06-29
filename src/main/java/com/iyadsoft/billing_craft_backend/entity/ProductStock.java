package com.iyadsoft.billing_craft_backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_stock", indexes = {
    @Index(name = "idx_productstock_username", columnList = "username"),
    @Index(name = "idx_productstock_productname", columnList = "productName"),
    @Index(name = "idx_productstock_category", columnList = "category"),
    @Index(name = "idx_productstock_productno", columnList = "productno"),
    @Index(name = "idx_productstock_supplier_invoice", columnList = "supplierInvoice"),
    @Index(name = "idx_productstock_date", columnList = "date")
})
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
    private Double pprice;
    private Double sprice;
    private String color;
    private String supplier;
    private String supplierInvoice;
    private String productno;
    private LocalDate date;
    private LocalTime time;

   @OneToMany(mappedBy = "productStock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductSale> productSale;
}
