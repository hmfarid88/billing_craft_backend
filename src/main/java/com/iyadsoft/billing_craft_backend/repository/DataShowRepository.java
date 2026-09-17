package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.entity.DataShow;


@Repository
public interface DataShowRepository extends JpaRepository<DataShow, Long> {
Optional<DataShow> findByUsername(String username);

    @Query("SELECT v.percent FROM DataShow v WHERE v.username = :username")
    Optional<Double> findPercentByUsername(@Param("username") String username);
}
