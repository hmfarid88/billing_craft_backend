package com.iyadsoft.billing_craft_backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pricedrop", indexes = {
    @Index(name = "idx_pricedrop_productname", columnList = "productName"),
    @Index(name = "idx_pricedrop_date", columnList = "date"),
    @Index(name = "idx_pricedrop_username", columnList = "username"),
    @Index(name = "idx_pricedrop_category", columnList = "category"),
    @Index(name = "idx_pricedrop_productno", columnList = "productno")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pricedrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String brand;
    private String productName;
    private Double oldpprice;
    private Double newpprice;
    private String supplier;
    private String productno;
    private LocalDate date;
    private String username;

}
