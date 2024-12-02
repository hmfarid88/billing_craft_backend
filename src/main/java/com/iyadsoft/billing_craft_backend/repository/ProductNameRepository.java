package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.ProductName;

public interface ProductNameRepository extends JpaRepository<ProductName, Long> {

    boolean existsByUsernameAndProductItem(String username, String productItem);
    void deleteByUsernameAndProductItem(String username, String productItem);

    List<ProductName> getProductsItemByUsername(String username);

    
}
