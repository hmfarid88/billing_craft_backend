package com.iyadsoft.billing_craft_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.Admin;
import com.iyadsoft.billing_craft_backend.entity.UserInfo;
import com.iyadsoft.billing_craft_backend.repository.AdminRepository;
import com.iyadsoft.billing_craft_backend.repository.UserInfoRepository;
import com.iyadsoft.billing_craft_backend.service.UserInfoService;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
        if (userInfoRepository.existsByUsername(userInfo.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username " + userInfo.getUsername() + " already exists!");
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setStatus("ON");
        UserInfo savedUser = userInfoRepository.save(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateUserStatus(@RequestParam String username, @RequestParam boolean status) {
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userInfo.setStatus(status ? "ON" : "OFF");
        userInfoRepository.save(userInfo);

        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/userChange")
    public ResponseEntity<String> updatePassword(@RequestParam String username, @RequestParam String newPassword) {
        boolean isUpdated = userInfoService.updatePassword(username, newPassword);
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    @PostMapping("/addAdminUser")
    public ResponseEntity<?> addAdminUser(@RequestBody Admin admin) {
        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new DuplicateEntityException("Username " + admin.getUsername() + " already exists !");
        }
        admin.setPassword(admin.getPassword());
        Admin savedUser = adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/adminChange")
    public ResponseEntity<String> updateAdminPassword(@RequestParam String username, @RequestParam String newPassword) {
        boolean isUpdated = userInfoService.updateAdminPassword(username, newPassword);
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    @GetMapping("/userLogin")
    public ResponseEntity<Map<String, String>> getUserInfo(@RequestParam String username,
            @RequestParam String password) {
        UserInfo user = userInfoRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User found");
            response.put("roles", user.getRoles());
            response.put("status", user.getStatus());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid credentials");
            return ResponseEntity.status(401).body(error);
        }
    }

    @GetMapping("/user/userList")
    public List<UserInfo> getUsers() {
        return userInfoRepository.findAll();
    }

    @PostMapping("/adminValidate")
    public ResponseEntity<?> validateAdmin(@RequestParam String username, @RequestParam String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            return ResponseEntity.status(404).body("Username not found!");
        }
        if (!admin.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid password!");
        }
        return ResponseEntity.ok("Valid admin");
    }
}
