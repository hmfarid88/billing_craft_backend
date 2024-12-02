package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.BrandName;

public interface BrandNameRepository extends JpaRepository<BrandName, Long> {

    List<BrandName> getBrandItemByUsername(String username);

    boolean existsByUsernameAndBrandItem(String username, String brandItem);

    void deleteByUsernameAndBrandItem(String username, String brandItem);
}
