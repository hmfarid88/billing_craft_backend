package com.iyadsoft.billing_craft_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "user_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private LocalDate billMonth;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.UNPAID;

    private LocalDate paymentDate;

    private String paymentMethod;
}
