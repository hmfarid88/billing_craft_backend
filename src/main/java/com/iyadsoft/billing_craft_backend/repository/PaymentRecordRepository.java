package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
                        "WHERE ps.sale_type = 'customer' AND ps.username = :username AND DATE(ps.date) < :date) + "
                        +
                        "(SELECT COALESCE(SUM(amount), 0) FROM profit_withdraw WHERE type='deposit' AND username = :username AND DATE(date) < :date) + "
                        +
                        "(SELECT COALESCE(SUM(c.vat_amount), 0) FROM customer c JOIN product_sale ps ON c.cid = ps.cid WHERE c.username = :username AND DATE(ps.date) < :date) "
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
                        "  (SELECT COALESCE(SUM(c.vat_amount), 0) FROM customer c JOIN product_sale ps ON c.cid = ps.cid WHERE c.username = :username) + "
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

        @Query(value = """
                        SELECT
                            u.username,

                            (
                                (
                                    SELECT COALESCE(SUM(pr.amount), 0)
                                    FROM payment_record pr
                                    WHERE pr.payment_type = 'receive'
                                    AND pr.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(sp.amount), 0)
                                    FROM supplier_payment sp
                                    WHERE sp.payment_type = 'receive'
                                    AND sp.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(ps.sprice - ps.discount - ps.offer), 0)
                                    FROM product_sale ps
                                    WHERE ps.sale_type = 'customer'
                                    AND ps.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(c.vat_amount), 0)
                                    FROM customer c
                                    JOIN product_sale ps ON c.cid = ps.cid
                                    WHERE c.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(pw.amount), 0)
                                    FROM profit_withdraw pw
                                    WHERE pw.type = 'deposit'
                                    AND pw.username = u.username
                                )
                            )

                            -

                            (
                                (
                                    SELECT COALESCE(SUM(e.amount), 0)
                                    FROM expense e
                                    WHERE e.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(pr.amount), 0)
                                    FROM payment_record pr
                                    WHERE pr.payment_type = 'payment'
                                    AND pr.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(sp.amount), 0)
                                    FROM supplier_payment sp
                                    WHERE sp.payment_type = 'payment'
                                    AND sp.username = u.username
                                )

                                +

                                (
                                    SELECT COALESCE(SUM(pw.amount), 0)
                                    FROM profit_withdraw pw
                                    WHERE pw.type = 'withdraw'
                                    AND pw.username = u.username
                                )
                            ) AS cashValue

                        FROM user_info u

                        WHERE u.owner_group = (
                            SELECT owner_group
                            FROM user_info
                            WHERE username = :username
                        )

                        ORDER BY u.username ASC
                        """, nativeQuery = true)
        List<Object[]> findGroupCashSummary(@Param("username") String username);

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

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PayRecevDetails(p.date, p.paymentNote, p.amount, 0.0, 0.0, 0.0) FROM PaymentRecord p WHERE p.paymentType='payment' AND p.username=:username AND p.paymentName=:paymentName AND p.date BETWEEN :fromDate AND :toDate ORDER BY p.date, p.id")
        List<PayRecevDetails> findPaymentsByUserAndPaymentName(@Param("username") String username,
                        @Param("paymentName") String paymentName, LocalDate fromDate, LocalDate toDate);

        @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PayRecevDetails(p.date, p.paymentNote, 0.0, p.amount, 0.0, 0.0) FROM PaymentRecord p WHERE p.paymentType='receive' AND p.username=:username AND p.paymentName=:paymentName AND p.date BETWEEN :fromDate AND :toDate ORDER BY p.date, p.id")
        List<PayRecevDetails> findReceivesByUserAndPaymentName(@Param("username") String username,
                        @Param("paymentName") String paymentName, LocalDate fromDate, LocalDate toDate);

        @Query("SELECT e FROM PaymentRecord e WHERE e.username = :username AND e.date >= :startDate ORDER BY e.date DESC")
        List<PaymentRecord> findLast7DaysOfficePaymentByUsername(String username, LocalDate startDate);

        Optional<PaymentRecord> findByIdAndUsername(Long id, String username);

        @Query("""
                        SELECT COALESCE(SUM(p.amount),0)
                        FROM PaymentRecord p
                        WHERE p.paymentType='payment' AND p.username=:username
                        AND p.paymentName=:paymentName
                        AND p.date<:fromDate
                        """)
        Double sumPaymentBeforeDate(String username, String paymentName, LocalDate fromDate);

        @Query("""
                        SELECT COALESCE(SUM(p.amount),0)
                        FROM PaymentRecord p
                        WHERE p.paymentType='receive' AND p.username=:username
                        AND p.paymentName=:paymentName
                        AND p.date<:fromDate
                        """)
        Double sumReceiveBeforeDate(String username, String paymentName, LocalDate fromDate);
}
