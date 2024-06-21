package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.entity.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    boolean existsByproductno(String productno);

    @Query("SELECT ps FROM ProductStock ps LEFT JOIN ps.productSale psale WHERE ps.username=:username AND psale IS NULL")
    List<ProductStock> getProductsStockByUsername(String username);

}
