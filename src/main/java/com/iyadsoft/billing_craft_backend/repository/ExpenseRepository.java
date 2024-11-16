package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.dto.PaymentDto;
import com.iyadsoft.billing_craft_backend.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT new com.iyadsoft.billing_craft_backend.dto.PaymentDto(e.date, e.expenseName, e.expenseNote, e.amount) "
            + "FROM Expense e WHERE e.username = :username AND e.date = :date")
    List<PaymentDto> findExpenseForToday(@Param("username") String username, @Param("date") LocalDate date);

    @Query("SELECT e FROM Expense e WHERE e.username = :username AND MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE)")
    List<Expense> findExpenseForCurrentMonth(@Param("username") String username);

    @Query("SELECT e FROM Expense e WHERE e.username = :username AND e.date BETWEEN :startDate AND :endDate")
    List<Expense> findExpenseForDatewise(@Param("username") String username, @Param("startDate")LocalDate startDate, @Param ("endDate") LocalDate endDate);

}
