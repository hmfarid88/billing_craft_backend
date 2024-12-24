package com.iyadsoft.billing_craft_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.entity.UserInfo;
import com.iyadsoft.billing_craft_backend.repository.AdminRepository;
import com.iyadsoft.billing_craft_backend.repository.UserInfoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserInfoService {
  
    private final UserInfoRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Autowired
    public UserInfoService(UserInfoRepository repository, BCryptPasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository=adminRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeAdminUser() {
        boolean adminExists = repository.existsByRoles("ROLE_ADMIN");
        if (!adminExists) {
            UserInfo adminUser = new UserInfo();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("485686@farid"));
            adminUser.setRoles("ROLE_ADMIN");
            repository.save(adminUser);
        }
    }
    public boolean updatePassword(String username, String newPassword) {
        String encryptedPassword = passwordEncoder.encode(newPassword);
        int rowsAffected = repository.updatePasswordByUsername(username, encryptedPassword);
        return rowsAffected > 0;
    }

    public boolean updateAdminPassword(String username, String newPassword) {
        int rowsAffected = adminRepository.updatePasswordByUsername(username, newPassword);
        return rowsAffected > 0;
    }

}
