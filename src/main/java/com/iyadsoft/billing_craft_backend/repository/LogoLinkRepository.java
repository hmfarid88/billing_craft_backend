package com.iyadsoft.billing_craft_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.LogoLink;

public interface LogoLinkRepository extends JpaRepository<LogoLink, Long> {

    Optional<LogoLink> findByUsername(String username);

}
