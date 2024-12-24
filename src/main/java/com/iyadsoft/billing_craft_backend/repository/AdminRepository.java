package com.iyadsoft.billing_craft_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.entity.Admin;

import jakarta.transaction.Transactional;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
    Admin findByUsername(String username);

    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Admin u SET u.password = :password WHERE u.username = :username")
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);
}
