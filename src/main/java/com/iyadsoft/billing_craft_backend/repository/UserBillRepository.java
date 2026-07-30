package com.iyadsoft.billing_craft_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.entity.BillStatus;
import com.iyadsoft.billing_craft_backend.entity.UserBill;

public interface UserBillRepository extends JpaRepository<UserBill, Long> {

    @Query("SELECT MAX(b.billMonth) FROM UserBill b WHERE b.username = :username")
    Optional<LocalDate> findLastBillMonth(@Param("username") String username);

    List<UserBill> findByUsernameAndStatus(String username, BillStatus unpaid);

}
