package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.WalletName;

public interface WalletNameRepository extends JpaRepository<WalletName, Long> {

   
    WalletName findByUsernameAndWalletName(String username, String paymentName);

    List<WalletName> getWalletNameByUsername(String username);

}
