package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.SmsPermission;

public interface SmsPermissionRepository extends JpaRepository<SmsPermission, Integer>{
    Optional<SmsPermission> findByUsername(String username);
}
