package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.ShopInfo;

public interface ShopInfoRepository extends JpaRepository<ShopInfo, Integer>{

    Optional<ShopInfo> findByUsername(String username);
    
}
