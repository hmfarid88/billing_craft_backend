package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO;
import com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO;
import com.iyadsoft.billing_craft_backend.entity.ProductSale;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
    @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerProductSaleDTO(c.cName, c.phoneNumber, c.address, ps.productName, ps.productno, ps.color, ps.sprice, s.discount, s.offer, s.saleDate, c.cid,  ps.proId, s.username) FROM ProductSale s JOIN s.customer c JOIN s.productStock ps WHERE s.username=:username")
    List<CustomerProductSaleDTO> getProductsSaleByUsername(String username);

    @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.InvoiceDataDTO(c.cName, c.phoneNumber, c.address, ps.brand, ps.productName, ps.productno, ps.color, s.saleDate, s.saleType, ps.pprice, ps.sprice, s.discount, s.offer, c.cardPay, c.vatAmount, c.dueAmount, c.cid) FROM ProductSale s JOIN s.customer c JOIN s.productStock ps WHERE s.username=:username and c.cid=:cid")
    List<InvoiceDataDTO> getInvoiceDataByUsername(String username, String cid);


}
