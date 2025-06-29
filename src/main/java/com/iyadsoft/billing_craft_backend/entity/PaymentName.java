package com.iyadsoft.billing_craft_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_name", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_payment_person", columnList = "paymentPerson")
       
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String paymentPerson;
    private String username;
}
