package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.entity.Vat;

public interface VatRepository extends JpaRepository<Vat, Integer> {

    Optional<Vat> findByUsername(String username);

    @Query("SELECT v.percent FROM Vat v WHERE v.username = :username")
    Optional<Double> findPercentByUsername(@Param("username") String username);

}
