package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PayRecevBalance;
import com.iyadsoft.billing_craft_backend.dto.PayRecevDetails;
import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.entity.PaymentRecord;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {

        @Query(value = "SELECT ( " +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE payment_type='receive' AND username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE payment_type='receive' AND username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(ps.sprice - ps.discount - ps.offer), 0) FROM product_sale ps "
                        +
                        "WHERE ps.sale_type = 'customer' AND ps.username = :username AND DATE(ps.date) < :date) + " +

                        "(SELECT COALESCE(SUM(amount), 0) FROM profit_withdraw WHERE type='deposit' AND username = :username AND DATE(date) < :date) "
                        +
                        ") - ( " +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM expense WHERE username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE payment_type='payment' AND username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE payment_type='payment' AND username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM profit_withdraw WHERE type='withdraw' AND username = :username AND DATE(date) < :date)) "
                        +
                        "AS total_amount", nativeQuery = true)
        Double findNetSumAmountBeforeToday(@Param("username") String username, @Param("date") LocalDate date);

        @Query(value = "SELECT ( " +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE payment_type='receive' AND username = :username) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE payment_type='receive' AND username = :username) + "
                        +
                        "  (SELECT COALESCE(SUM(ps.sprice - ps.discount - ps.offer), 0) FROM product_sale ps WHERE ps.sale_type = 'customer' AND ps.username = :username) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM profit_withdraw WHERE type='deposit' AND username = :username) "
                        +
                        ") - ( " +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM expense WHERE username = :username) + " +

                        "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE payment_type='payment' AND username = :username) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE payment_type='payment' AND username = :username) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM profit_withdraw WHERE type='withdraw' AND username = :username) "
                        +
                        ") AS total_amount", nativeQuery = true)
        Double findCashToday(@Param("username") String username);

        @Query("SELECT SUM(p.amount) FROM PaymentRecord p WHERE p.paymentType='payment' AND p.username=:username ")
        Double findAllPaymentSum(@Param("username") String username);

        @Query("SELECT SUM(p.amount) FROM PaymentRecord p WHERE p.paymentType='receive' AND p.username=:username ")
        Double findAllReceiveSum(@Param("username") String username);

        @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(p.date, p.paymentName, p.paymentNote, p.amount) "
                        + "FROM PaymentRecord p WHERE p.paymentType='payment' AND p.username=:username AND p.date = :date")
        List<PaymentDto> findPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);

        @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.ReceiveDto(p.date, p.paymentName, p.paymentNote, p.amount) "
                        + "FROM PaymentRecord p WHERE p.paymentType='receive' AND p.username=:username AND p.date = :date")
        List<ReceiveDto> findReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PayRecevBalance(" +
                        "pr.paymentName, " +
                        "SUM(CASE WHEN pr.paymentType = 'payment' THEN pr.amount ELSE 0 END), " +
                        "SUM(CASE WHEN pr.paymentType = 'receive' THEN pr.amount ELSE 0 END)) " +
                        "FROM PaymentRecord pr " +
                        "WHERE pr.username = :username " +
                        "GROUP BY pr.paymentName")
        List<PayRecevBalance> findPayRecevSummaryBalances(@Param("username") String username);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PayRecevDetails( " +
                        "pr.date, pr.paymentNote, " +
                        "SUM(CASE WHEN pr.paymentType = 'payment' THEN pr.amount ELSE 0 END) AS payment, " +
                        "SUM(CASE WHEN pr.paymentType = 'receive' THEN pr.amount ELSE 0 END) AS receive, " +
                        "(SELECT SUM(CASE WHEN pr2.paymentType = 'payment' THEN pr2.amount ELSE 0 END) - " +
                        "        SUM(CASE WHEN pr2.paymentType = 'receive' THEN pr2.amount ELSE 0 END) " +
                        " FROM PaymentRecord pr2 " +
                        " WHERE pr2.username = :username AND pr2.paymentName = :paymentName " +
                        " AND (pr2.date < pr.date OR (pr2.date = pr.date AND pr2.id <= pr.id)) " +
                        ") AS balance " +
                        ") " +
                        "FROM PaymentRecord pr " +
                        "WHERE pr.username = :username AND pr.paymentName = :paymentName " +
                        "GROUP BY pr.date, pr.paymentNote, pr.id " +
                        "ORDER BY pr.date, pr.id")
        List<PayRecevDetails> findDatePaymentReceiveAndNoteByUserAndPaymentName(
                        @Param("username") String username,
                        @Param("paymentName") String paymentName);

}
