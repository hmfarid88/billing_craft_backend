package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.dto.CustomerDto;
import com.iyadsoft.billing_craft_backend.dto.CustomerVatSaleDTO;
import com.iyadsoft.billing_craft_backend.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Modifying
    @Query("DELETE FROM Customer c WHERE c.cid = :cid")
    void deleteByCid(@Param("cid") String cid);

    @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerDto(c.cName, c.phoneNumber, c.cid, c.address, c.soldby) FROM Customer c WHERE c.username = :username AND c.phoneNumber = :phoneNumber")
    List<CustomerDto> findByUsernameAndPhoneNumber(String username, String phoneNumber);

  @Query("""
    SELECT new com.iyadsoft.billing_craft_backend.dto.CustomerVatSaleDTO(
        c.cid, c.cName, c.phoneNumber, c.vatAmount, ps.date
    )
    FROM Customer c
    JOIN c.productSale ps
    WHERE c.username = :username AND c.vatAmount > 0 GROUP BY c.cid, c.cName, c.phoneNumber, c.vatAmount
""")
List<CustomerVatSaleDTO> findCustomerVatSalesByUsername(@Param("username") String username);

   
}
