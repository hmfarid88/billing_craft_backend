package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto;
import com.iyadsoft.billing_craft_backend.entity.SupplierPayment;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
  @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(s.date, s.supplierName, s.note, s.amount) "
      + "FROM SupplierPayment s WHERE s.paymentType='payment' AND s.username = :username AND s.date = :date")
  List<PaymentDto> findSupplierPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);

  @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.ReceiveDto(s.date, s.supplierName, s.note, s.amount) "
      + "FROM SupplierPayment s WHERE s.paymentType='receive' AND s.username = :username AND s.date = :date")
  List<ReceiveDto> findSupplierReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);

  
  @Query("SELECT " +
      "COALESCE(SUM(CASE WHEN sp.paymentType = 'payment' THEN sp.amount ELSE 0 END), 0) AS totalPayment " +
      "FROM SupplierPayment sp " +
      "WHERE sp.username = :username AND sp.supplierName = :supplierName")
  Double findTotalPaymentByUsernameAndSupplier(
      @Param("username") String username,
      @Param("supplierName") String supplierName);

  
  @Query("SELECT " +
      "COALESCE(SUM(CASE WHEN sp.paymentType = 'receive' THEN sp.amount ELSE 0 END), 0) AS totalReceive " +
      "FROM SupplierPayment sp " +
      "WHERE sp.username = :username AND sp.supplierName = :supplierName")
  Double findTotalReceiveByUsernameAndSupplier(
      @Param("username") String username,
      @Param("supplierName") String supplierName);

      @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto(sp.date, 'Payment', 0.0, 0.0, sp.amount, 0.0, sp.note) " +
      "FROM SupplierPayment sp " +
      "WHERE sp.paymentType = 'payment' AND sp.username = :username AND sp.supplierName = :supplierName")
      List<SupplierDetailsDto> findDetailsPaymentByUsernameAndSupplier(
      @Param("username") String username,
      @Param("supplierName") String supplierName);

      @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.SupplierDetailsDto(sp.date, 'Receive', 0.0, 0.0, 0.0, sp.amount, sp.note) " +
      "FROM SupplierPayment sp " +
      "WHERE sp.paymentType = 'receive' AND sp.username = :username AND sp.supplierName = :supplierName")
  List<SupplierDetailsDto> findDetailsReceiveByUsernameAndSupplier(
      @Param("username") String username,
      @Param("supplierName") String supplierName);

}
