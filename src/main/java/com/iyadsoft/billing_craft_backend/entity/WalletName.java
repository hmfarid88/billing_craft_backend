package com.iyadsoft.billing_craft_backend.entity;

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
@Table(name = "wallet_name", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_wallet_name", columnList = "walletName")

})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String walletName;
    private Double rate;
    private String username;
}
