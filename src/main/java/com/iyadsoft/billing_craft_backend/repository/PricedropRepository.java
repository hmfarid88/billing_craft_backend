package com.iyadsoft.billing_craft_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iyadsoft.billing_craft_backend.entity.Pricedrop;

@Repository
public interface PricedropRepository extends JpaRepository<Pricedrop, Long> {
    boolean existsByUsernameAndProductno(String username, String productno);
}
