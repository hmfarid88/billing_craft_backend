package com.iyadsoft.billing_craft_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iyadsoft.billing_craft_backend.entity.UserInfo;

import jakarta.transaction.Transactional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
   
    UserInfo findByUsername(String username);

    boolean existsByUsername(String username);

    List<UserInfo> findByRoles(String string);

    boolean existsByRoles(String string);
    
    @Transactional
    @Modifying
    @Query("UPDATE UserInfo u SET u.password = :password WHERE u.username = :username")
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    @Query("""
    SELECT u
    FROM UserInfo u
    WHERE u.ownerGroup = (
        SELECT t.ownerGroup
        FROM UserInfo t
        WHERE t.username = :username
    )
    ORDER BY u.username ASC
""")
List<UserInfo> findUsersBySameOwnerGroup(@Param("username") String username);
}
