package com.iyadsoft.billing_craft_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iyadsoft.billing_craft_backend.controller.DuplicateEntityException;
import com.iyadsoft.billing_craft_backend.entity.UserInfo;
import com.iyadsoft.billing_craft_backend.repository.UserInfoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = repository.findByName(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }

    public String addUser(UserInfo userInfo) {
        if (repository.findByName(userInfo.getName()).isPresent()) {
            throw new DuplicateEntityException("This user is already exists !");
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }

    @PostConstruct
    @Transactional
    public void initializeAdminUser() {
        boolean adminExists = repository.existsByRoles("ROLE_ADMIN");
        if (!adminExists) {
            UserInfo adminUser = new UserInfo();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setName("admin");
            adminUser.setPassword(encoder.encode("485686@farid"));
            adminUser.setRoles("ROLE_ADMIN");
            repository.save(adminUser);
        }
    }

}
