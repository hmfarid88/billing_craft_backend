package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.iyadsoft.billing_craft_backend.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
   
    UserInfo findByUsername(String username);

    boolean existsByUsername(String username);

    List<UserInfo> findByRoles(String string);

    boolean existsByRoles(String string);
}
