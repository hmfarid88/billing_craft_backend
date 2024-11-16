package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    
    @Query("SELECT ps FROM ProductStock ps LEFT JOIN ps.productSale psale WHERE ps.username=:username AND psale IS NULL")
    List<ProductStock> getProductsStockByUsername(String username);

    boolean existsByUsernameAndProductno(String username, String productno);

      @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO(" +
           "p.category, p.brand, p.productName, " +
           "SUM(CASE WHEN p.date < :today THEN 1 ELSE 0 END) AS countBeforeToday, " +
           "SUM(CASE WHEN p.date = :today THEN 1 ELSE 0 END) AS countToday) " +
           "FROM ProductStock p " +
           "LEFT JOIN p.productSale ps " +
           "WHERE p.username = :username " +
           "AND ps.id IS NULL " +  // Ensures productno not in productSale
           "GROUP BY p.category, p.brand, p.productName")
    List<ProductStockCountDTO> countProductByUsernameGroupByCategoryBrandProductName(
            @Param("username") String username,
            @Param("today") LocalDate today);

}
