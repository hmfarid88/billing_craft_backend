package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.ColorName;

public interface ColorNameRepository extends JpaRepository<ColorName, Long> {

    boolean existsByUsernameAndColorItem(String username, String colorItem);

    void deleteByUsernameAndColorItem(String username, String colorItem);

    List<ColorName> getColorItemByUsername(String username);

}
