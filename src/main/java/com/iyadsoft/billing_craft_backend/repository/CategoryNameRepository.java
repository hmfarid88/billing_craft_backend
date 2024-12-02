package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.CategoryName;

public interface CategoryNameRepository extends JpaRepository<CategoryName, Long>{

    List<CategoryName> getCategoryItemByUsername(String username);

    boolean existsByUsernameAndCategoryItem(String username, String categoryItem);
    
    void deleteByUsernameAndCategoryItem(String username, String categoryItem);
}
