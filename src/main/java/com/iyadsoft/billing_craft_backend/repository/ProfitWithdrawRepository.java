package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.entity.ProfitWithdraw;

public interface ProfitWithdrawRepository extends JpaRepository<ProfitWithdraw, Long>{

    @Query(value = "SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(p.date, 'profit withdraw', p.note, p.amount) "
            + "FROM ProfitWithdraw p WHERE p.username=:username AND p.date = :date")
    List<PaymentDto> findProfitWithdrawForToday(@Param("username") String username, @Param("date") LocalDate date);
}