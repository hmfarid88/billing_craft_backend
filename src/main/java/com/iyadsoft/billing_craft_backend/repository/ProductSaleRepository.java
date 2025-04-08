package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.dto.CashbookSaleDto;
import com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO;
import com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO;
import com.iyadsoft.billing_craft_backend.dto.LossProfitAnalysis;
import com.iyadsoft.billing_craft_backend.dto.ProfitItemDto;
import com.iyadsoft.billing_craft_backend.dto.SixMonthAnalysis;
import com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO(s.customer.cName, s.customer.phoneNumber, s.customer.address, s.customer.soldby, s.productStock.category, s.productStock.brand, s.productStock.productName, s.productStock.productno, s.productStock.color, s.productStock.pprice, s.sprice, s.discount, s.offer, s.date, s.time, s.customer.cid,  s.productStock.proId, s.username) FROM ProductSale s WHERE s.username=:username AND s.saleType='customer' AND s.date = CURRENT_DATE")
        List<CustomerProductSaleDTO> getProductsSaleByUsername(String username);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO(s.customer.cName, s.customer.phoneNumber, s.customer.address, s.customer.soldby, s.productStock.category, s.productStock.brand, s.productStock.productName, s.productStock.productno, s.productStock.color, s.productStock.pprice, s.sprice, s.discount, s.offer, s.date, s.time, s.customer.cid, s.productStock.proId, s.username) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.username = :username AND s.saleType = 'customer' " +
                        "AND MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE)")
        List<CustomerProductSaleDTO> getProductsSaleByUsernameForCurrentMonth(String username);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO(s.customer.cName, s.customer.phoneNumber, s.customer.address, s.customer.soldby, s.productStock.category, s.productStock.brand, s.productStock.productName, s.productStock.productno, s.productStock.color, s.productStock.pprice, s.sprice, s.discount, s.offer, s.date, s.time, s.customer.cid, s.productStock.proId, s.username) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.saleType = 'customer' " +
                        "AND s.username = :username AND s.date BETWEEN :startDate AND :endDate")
        List<CustomerProductSaleDTO> getProductsSaleByUsernameDatewise(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ProfitItemDto(s.productStock.category, s.productStock.brand, s.productStock.productName, COUNT(s.productStock.productno) as qty,  SUM(s.productStock.pprice) as pprice, SUM(s.sprice) as sprice, SUM(s.discount) as discount ) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.username = :username AND s.saleType = 'customer' " +
                        "AND MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE) GROUP BY s.productStock.category, s.productStock.brand, s.productStock.productName ")
        List<ProfitItemDto> getProfitSaleByUsernameForCurrentMonth(String username);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ProfitItemDto(s.productStock.category, s.productStock.brand, s.productStock.productName, COUNT(s.productStock.productno) as qty,  SUM(s.productStock.pprice) as pprice, SUM(s.sprice) as sprice, SUM(s.discount) as discount ) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.username = :username AND s.saleType = 'customer' " +
                        "AND YEAR(s.date) = :year AND MONTH(s.date) = :month GROUP BY s.productStock.category, s.productStock.brand, s.productStock.productName ")
        List<ProfitItemDto> getSelectedProfitSaleByUsername(String username, int year, int month);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ProfitItemDto(s.productStock.category, s.productStock.brand, s.productStock.productName, COUNT(s.productStock.productno) as qty,  SUM(s.productStock.pprice) as pprice, SUM(s.sprice) as sprice, SUM(s.discount) as discount ) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.username = :username AND s.saleType = 'customer' " +
                        "AND s.date BETWEEN :startDate AND :endDate GROUP BY s.productStock.category, s.productStock.brand, s.productStock.productName ")
        List<ProfitItemDto> getDatewiseProfitSaleByUsername(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO(s.customer.cName, s.customer.phoneNumber, s.customer.address, s.customer.soldby, s.productStock.category, s.productStock.brand, s.productStock.productName, s.productStock.productno, s.productStock.color, s.productStock.pprice, s.productStock.sprice, s.discount, s.offer, s.date, s.time, s.customer.cid, s.productStock.proId, s.username) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.username = :username AND s.saleType = 'vendor' " +
                        "AND MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE)")
        List<CustomerProductSaleDTO> getVendorSaleByUsernameForCurrentMonth(String username);

        
        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO(s.customer.cName, s.customer.phoneNumber, s.customer.address, s.customer.soldby, s.productStock.category, s.productStock.brand, s.productStock.productName, s.productStock.productno, s.productStock.color, s.productStock.pprice, s.productStock.sprice, s.discount, s.offer, s.date, s.time, s.customer.cid, s.productStock.proId, s.username) "
                        +
                        "FROM ProductSale s " +
                        "WHERE s.saleType = 'vendor' " +
                        "AND s.username = :username AND s.date BETWEEN :startDate AND :endDate")
        List<CustomerProductSaleDTO> getVendorSaleByUsernameDatewise(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO(c.cName, c.phoneNumber, c.address, c.soldby, ps.brand, ps.productName, ps.productno, ps.color, s.date, s.time, s.saleType, ps.pprice, s.sprice, s.discount, s.offer, c.cardPay, c.vatAmount, c.received, c.cid, s.saleId) FROM ProductSale s JOIN s.customer c JOIN s.productStock ps WHERE s.username=:username and c.cid=:cid")
        List<InvoiceDataDTO> getInvoiceDataByUsername(String username, String cid);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CashbookSaleDto(ps.date, ps.customer.cid, sum(ps.productStock.sprice+ps.customer.vatAmount-ps.discount-ps.offer-ps.customer.cardPay) as value) "
                        +
                        "FROM ProductSale ps " +
                        "WHERE ps.saleType = 'customer' AND (ps.productStock.sprice-ps.discount-ps.offer)>0 AND ps.username=:username AND ps.date=CURRENT_DATE group by ps.date, ps.customer.cid")
        List<CashbookSaleDto> findCustomerSalesDetails(@Param("username") String username);

        @Query("SELECT SUM(COALESCE(ps.productStock.pprice, 0.0)) AS totalSoldValue " +
                        "FROM ProductSale ps " +
                        "WHERE ps.saleType='vendor' AND ps.username = :username AND ps.customer.cName = :cName")
        Double findTotalSoldValueByUsernameAndSupplier(@Param("username") String username, @Param("cName") String supplier);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto(ps.date, ps.customer.cid, 0.0, SUM(ps.productStock.pprice), 0.0, 0.0, 'No') "
                        +
                        "FROM ProductSale ps " +
                        "WHERE ps.saleType='vendor' AND ps.username = :username AND ps.customer.cName = :supplierName "
                        +
                        "GROUP BY ps.date, ps.customer.cid")
        List<SupplierDetailsDto> findProductSalesByUsernameAndSupplierName(String username, String supplierName);

        @Query("SELECT ps.customer.cid FROM ProductSale ps WHERE ps.username = :username AND ps.productStock.productno = :productno")
        String findCidByUsernameAndProductno(@Param("username") String username, @Param("productno") String productno);

        @Query("SELECT COUNT(ps) FROM ProductSale ps WHERE ps.customer.cid = :cid")
        long countSalesByCustomerCid(@Param("cid") String cid);

        @Modifying
        @Query("DELETE FROM ProductSale ps WHERE ps.productStock.productno = :productno AND ps.username = :username")
        void deleteByUsernameAndProductno(@Param("username") String username, @Param("productno") String productno);

        @Query("SELECT ps.customer.cid FROM ProductSale ps WHERE ps.username = :username ORDER BY ps.saleId DESC LIMIT 1")
        String findLastCustomerCidByUsername(@Param("username") String username);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.SixMonthAnalysis(MONTHNAME(ps.date) AS month, SUM(COALESCE(ps.sprice, 0) - COALESCE(ps.discount, 0)) AS Value) " +
        "FROM ProductSale ps " +
        "WHERE ps.username = :username AND ps.saleType = 'customer' " +
        "AND ps.date BETWEEN :startDate AND :endDate " +
        "GROUP BY MONTHNAME(ps.date) ")
         List<SixMonthAnalysis> findLastSixMonthsSalesByUser(
         @Param("username") String username,
         @Param("startDate") LocalDate startDate,
         @Param("endDate") LocalDate endDate
 );


@Query("SELECT new com.iyadsoft.billing_craft_backend.dto.LossProfitAnalysis(" +
       "FUNCTION('MONTHNAME', s.date), " + 
       "SUM(CASE WHEN (s.sprice > s.productStock.pprice) THEN (s.sprice - s.discount - s.productStock.pprice) ELSE 0 END), " +
       "SUM(CASE WHEN (s.sprice < s.productStock.pprice) THEN (s.productStock.pprice - s.sprice - s.discount) ELSE 0 END)) " +
       "FROM ProductSale s " +
       "WHERE s.username = :username AND s.saleType = 'customer' " +
       "AND s.date >= :startDate " +
       "GROUP BY FUNCTION('MONTHNAME', s.date), MONTH(s.date) " + 
       "ORDER BY MONTH(s.date)")
List<LossProfitAnalysis> findLastTwelveMonthsProfitLoss(@Param("username") String username, @Param("startDate") LocalDate startDate);

@Query("SELECT new com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO(c.cName, c.phoneNumber, c.address, c.soldby, ps.brand, ps.productName, ps.productno, ps.color, s.date, s.time, s.saleType, ps.pprice, s.sprice, s.discount, s.offer, c.cardPay, c.vatAmount, c.received, c.cid, s.saleId) " +
       "FROM ProductSale s " +
       "JOIN s.customer c " +
       "JOIN s.productStock ps " +
       "WHERE s.username = :username AND s.saleId < :saleId " +
       "ORDER BY s.saleId DESC LIMIT 1")
Optional<InvoiceDataDTO> getPreviousInvoiceBySaleId(String username, Long saleId);

@Query("SELECT new com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO(c.cName, c.phoneNumber, c.address, c.soldby, ps.brand, ps.productName, ps.productno, ps.color, s.date, s.time, s.saleType, ps.pprice, s.sprice, s.discount, s.offer, c.cardPay, c.vatAmount, c.received, c.cid, s.saleId) " +
       "FROM ProductSale s " +
       "JOIN s.customer c " +
       "JOIN s.productStock ps " +
       "WHERE s.username = :username AND s.saleId > :saleId " +
       "ORDER BY s.saleId ASC LIMIT 1")
Optional<InvoiceDataDTO> getNextInvoiceBySaleId(String username, Long saleId);


}
