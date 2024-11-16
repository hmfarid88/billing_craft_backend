package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.entity.PaymentRecord;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
      
    @Query(value = "SELECT ( " +
    "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE payment_type='receive' AND username = :username AND DATE(date) < :date) + " +
    "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE payment_type='receive' AND username = :username AND DATE(date) < :date) + " +
    "  (SELECT COALESCE(SUM(pst.sprice - ps.discount - ps.offer), 0) FROM product_sale ps JOIN product_stock pst ON ps.pro_id = pst.pro_id " +
    "WHERE ps.sale_type = 'customer' AND ps.username ='mobile home' AND DATE(ps.date) < :date)" +
    ") - ( " +
    "  (SELECT COALESCE(SUM(amount), 0) FROM expense WHERE username = :username AND DATE(date) < :date) + " +
    "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE payment_type='payment' AND username = :username AND DATE(date) < :date) + " +
    "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE payment_type='payment' AND username = :username AND DATE(date) < :date) + " +
    "  (SELECT COALESCE(SUM(amount), 0) FROM profit_withdraw WHERE username = :username AND DATE(date) < :date)) " +
    "AS total_amount", nativeQuery = true)
Double findNetSumAmountBeforeToday(@Param("username") String username, @Param("date") LocalDate date);

 @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(p.date, p.paymentName, p.paymentNote, p.amount) "
            + "FROM PaymentRecord p WHERE p.paymentType='payment' AND p.username=:username AND p.date = :date")
 List<PaymentDto> findPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);

 @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.ReceiveDto(p.date, p.paymentName, p.paymentNote, p.amount) "
            + "FROM PaymentRecord p WHERE p.paymentType='receive' AND p.username=:username AND p.date = :date")
 List<ReceiveDto> findReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);




}
