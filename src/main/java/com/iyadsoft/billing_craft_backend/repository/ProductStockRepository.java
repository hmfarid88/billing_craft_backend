package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO;
import com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto;
import com.iyadsoft.billing_craft_backend.dto.UpdateableStock;
import com.iyadsoft.billing_craft_backend.entity.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

      @Query("SELECT ps FROM ProductStock ps LEFT JOIN ps.productSale psale WHERE ps.username=:username AND psale IS NULL")
      List<ProductStock> getProductsStockByUsername(String username);

      @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.UpdateableStock(ps.supplier, ps.productName, ps.pprice, COUNT(ps.productno)) FROM ProductStock ps LEFT JOIN ps.productSale psale WHERE ps.username=:username AND psale IS NULL GROUP BY ps.supplier, ps.productName, ps.pprice")
      List<UpdateableStock> getProductsStockByUsernameAndSupplier(String username);

      boolean existsByUsernameAndProductno(String username, String productno);

      @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ProductStockCountDTO(" +
                  "p.category, p.brand, p.productName, " +
                  "SUM(CASE WHEN p.date < :today THEN 1 ELSE 0 END) AS countBeforeToday, " +
                  "SUM(CASE WHEN p.date = :today THEN 1 ELSE 0 END) AS countToday) " +
                  "FROM ProductStock p " +
                  "LEFT JOIN p.productSale ps " +
                  "WHERE p.username = :username " +
                  "AND ps.id IS NULL " +
                  "GROUP BY p.category, p.brand, p.productName")
      List<ProductStockCountDTO> countProductByUsernameGroupByCategoryBrandProductName(
                  @Param("username") String username,
                  @Param("today") LocalDate today);

      @Query(value = "SELECT DISTINCT ps.supplier AS supplierName " +
                  "FROM product_stock ps " +
                  "WHERE ps.username=:username AND ps.supplier IS NOT NULL " +
                  "UNION " +
                  "SELECT DISTINCT c.c_name AS supplierName " +
                  "FROM customer c " +
                  "JOIN product_sale psale ON psale.cid = c.cid " +
                  "WHERE psale.username=:username AND psale.sale_type = 'vendor' AND c.c_name IS NOT NULL " +
                  "UNION " +
                  "SELECT DISTINCT sp.supplier_name AS supplierName " +
                  "FROM supplier_payment sp " +
                  "WHERE sp.username=:username AND sp.supplier_name IS NOT NULL", nativeQuery = true)
      List<String> findAllDistinctSupplierNames(@Param("username") String username);

      @Query("SELECT SUM(COALESCE(ps.pprice, 0.0)) AS totalProductValue " +
                  "FROM ProductStock ps " +
                  "WHERE ps.username = :username AND ps.supplier = :supplier")
      Double findTotalProductValueByUsernameAndSupplier(@Param("username") String username,
                  @Param("supplier") String supplier);

      @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto(ps.date, ps.supplierInvoice, SUM(ps.pprice), 0.0, 0.0, 0.0) FROM ProductStock ps WHERE ps.username = :username AND ps.supplier = :supplierName GROUP BY ps.date, ps.supplierInvoice")
      List<SupplierDetailsDto> findProductDetailsByUsernameAndSupplierName(String username, String supplierName);

      @Query("SELECT ps FROM ProductStock ps LEFT JOIN ps.productSale psale WHERE ps.username=:username AND ps.supplier=:supplier AND ps.productName=:productName AND ps.pprice=:pprice AND psale IS NULL")
      List<ProductStock> findByUsernameAndSupplierAndProductNameAndPprice(String username, String supplier, String productName, Double pprice);
}
