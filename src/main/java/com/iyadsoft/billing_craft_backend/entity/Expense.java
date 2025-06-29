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
@Table(name = "expense", indexes = {
    @Index(name = "idx_expense_username", columnList = "username"),
    @Index(name = "idx_expense_date", columnList = "date"),
    @Index(name = "idx_expense_name", columnList = "expenseName")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String expenseName;
    private String expenseNote;
    private Double amount;
    private String username;
}
