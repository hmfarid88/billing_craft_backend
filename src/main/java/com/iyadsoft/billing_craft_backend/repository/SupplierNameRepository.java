package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.SupplierName;

public interface SupplierNameRepository extends JpaRepository<SupplierName, Long> {

    boolean existsByUsernameAndSupplierItem(String username, String supplierItem);

    void deleteByUsernameAndSupplierItem(String username, String supplierItem);

    List<SupplierName> getSupplierItemByUsername(String username);

}
