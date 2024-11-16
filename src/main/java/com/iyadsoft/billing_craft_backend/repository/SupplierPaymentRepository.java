package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.entity.SupplierPayment;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long>{
     @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(s.date, s.supplierName, s.note, s.amount) "
      + "FROM SupplierPayment s WHERE s.paymentType='payment' AND s.username = :username AND s.date = :date")
  List<PaymentDto> findSupplierPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);

     @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ReceiveDto(s.date, s.supplierName, s.note, s.amount) "
      + "FROM SupplierPayment s WHERE s.paymentType='receive' AND s.username = :username AND s.date = :date")
  List<ReceiveDto> findSupplierReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);
}
