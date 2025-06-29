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
@Table(name = "profit_withdraw", indexes = {
    @Index(name = "idx_withdraw_username", columnList = "username"),
    @Index(name = "idx_withdraw_date", columnList = "date"),
    @Index(name = "idx_withdraw_year_month", columnList = "year, month"),
    @Index(name = "idx_withdraw_type", columnList = "type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitWithdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int year;
    private int month;
    private String type;
    private String note;
    private Double amount;
    private String username;
}
