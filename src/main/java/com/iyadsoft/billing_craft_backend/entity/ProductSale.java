package com.iyadsoft.billing_craft_backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;
    private String saleType;
    private Double sprice;
    private Double discount;
    private Double offer;
    private LocalDate date;
    private LocalTime time;
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
    private ProductStock productStock;

       
}
