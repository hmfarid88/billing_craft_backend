package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.dto.ReceiveDto;
import com.iyadsoft.billing_craft_backend.entity.ProfitWithdraw;

public interface ProfitWithdrawRepository extends JpaRepository<ProfitWithdraw, Long>{

    @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(p.date, 'profit withdraw', p.note, p.amount) "
            + "FROM ProfitWithdraw p WHERE p.type='withdraw' AND p.username=:username AND p.date = :date")
    List<PaymentDto> findProfitWithdrawForToday(@Param("username") String username, @Param("date") LocalDate date);

    @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(p.date, 'profit deposit', p.note, p.amount) "
            + "FROM ProfitWithdraw p WHERE p.type='deposit' AND p.username=:username AND p.date = :date")
    List<ReceiveDto> findProfitDepositForToday(@Param("username") String username, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM ProfitWithdraw p WHERE p.type='withdraw' AND p.username=:username AND p.year = :year AND p.month = :month")
    Double findCurrentMonthWithdrawSum(@Param("username") String username, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM ProfitWithdraw p WHERE p.type='deposit' AND p.username=:username AND p.year = :year AND p.month = :month")
    Double findCurrentMonthDepositSum(@Param("username") String username, @Param("year") int year, @Param("month") int month);

    List<ProfitWithdraw> findByUsername(String username);
}
