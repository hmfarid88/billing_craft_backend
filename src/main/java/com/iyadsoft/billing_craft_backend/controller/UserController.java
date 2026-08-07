package com.iyadsoft.billing_craft_backend.controller;

import java.math.BigDecimal;
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
import org.springframework.web.client.RestTemplate;

import com.iyadsoft.billing_craft_backend.entity.Admin;
import com.iyadsoft.billing_craft_backend.entity.BillStatus;
import com.iyadsoft.billing_craft_backend.entity.UserBill;
import com.iyadsoft.billing_craft_backend.entity.UserInfo;
import com.iyadsoft.billing_craft_backend.repository.AdminRepository;
import com.iyadsoft.billing_craft_backend.repository.UserBillRepository;
import com.iyadsoft.billing_craft_backend.repository.UserInfoRepository;
import com.iyadsoft.billing_craft_backend.service.MissingBill;
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

    @Autowired
    private MissingBill missingBill;

    @Autowired
    private UserBillRepository userBillRepository;

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
        if (userInfoRepository.existsByUsername(userInfo.getUsername())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Username " + userInfo.getUsername() + " already exists!, Try another.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
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
            response.put("roles", user.getRoles());
            response.put("status", user.getStatus());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Sorry, login fail. Try again !");
            return ResponseEntity.status(401).body(error);
        }
    }

    // @GetMapping("/userLogin")
    // public ResponseEntity<Map<String, String>> getUserInfo(@RequestParam String
    // username, @RequestParam String password) {
    // UserInfo user = userInfoRepository.findByUsername(username);

    // if (user != null && passwordEncoder.matches(password, user.getPassword())) {
    // // ✅ Generate missing bills if necessary
    // missingBill.ensureMissingBills(user);

    // Map<String, String> response = new HashMap<>();
    // response.put("roles", user.getRoles());
    // response.put("status", user.getStatus());
    // return ResponseEntity.ok(response);
    // } else {
    // Map<String, String> error = new HashMap<>();
    // error.put("message", "Sorry, login failed. Try again!");
    // return ResponseEntity.status(401).body(error);
    // }
    // }

    // @GetMapping("/userLogin")
    // public ResponseEntity<Map<String, String>> getUserInfo(
    // @RequestParam String username,
    // @RequestParam String password) {

    // UserInfo user = userInfoRepository.findByUsername(username);
    // Map<String, String> response = new HashMap<>();

    // if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
    // response.put("message", "Sorry, login failed. Try again!");
    // return ResponseEntity.status(401).body(response);
    // }

    // // 🔁 Step 1: Generate missing bills if any
    // missingBill.ensureMissingBills(user);

    // // 🔐 Step 2: Check if user is allowed to log in
    // List<UserBill> unpaidBills =
    // userBillRepository.findByUsernameAndStatus(username, BillStatus.UNPAID);
    // long unpaidMonths = unpaidBills.stream()
    // .map(UserBill::getBillMonth)
    // .distinct()
    // .count();

    // if (unpaidMonths >= 1) {
    // double totalDue = unpaidBills.stream()
    // .map(UserBill::getAmount)
    // .mapToDouble(BigDecimal::doubleValue)
    // .sum();

    // response.put("message", "Access suspended! You have unpaid bills.");
    // response.put("dueAmount", String.valueOf(totalDue));
    // response.put("redirectTo", "/payment-due");
    // return ResponseEntity.status(403).body(response);
    // }

    // // ✅ Allow login
    // response.put("roles", user.getRoles());
    // response.put("status", user.getStatus());
    // return ResponseEntity.ok(response);
    // }

    // updated
    // @GetMapping("/userLogin")
    // public ResponseEntity<Map<String, Object>> getUserInfo(
    // @RequestParam String username,
    // @RequestParam String password) {

    // UserInfo user = userInfoRepository.findByUsername(username);
    // Map<String, Object> response = new HashMap<>();

    // // ❌ Invalid user
    // if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
    // response.put("message", "Sorry, login failed. Try again!");
    // return ResponseEntity.status(401).body(response);
    // }

    // // 🔥 CALL BILLING SERVICE API
    // String billingUrl =
    // "http://auth.iyadsoft.com/api/subscription/check?username=" + username;

    // RestTemplate restTemplate = new RestTemplate();
    // Map billingData;

    // try {
    // ResponseEntity<Map> billingResponse = restTemplate.getForEntity(billingUrl,
    // Map.class);
    // billingData = billingResponse.getBody();
    // } catch (Exception e) {
    // response.put("message", "Billing service not reachable!");
    // return ResponseEntity.status(500).body(response);
    // }

    // String billingStatus = (String) billingData.get("status");

    // // ❌ NOT PAID / EXPIRED
    // if (!"ACTIVE".equalsIgnoreCase(billingStatus)) {

    // response.put("message", "Access suspended! You have unpaid bills.");
    // response.put("dueAmount", String.valueOf(billingData.get("dueAmount")));
    // response.put("redirectTo", "/payment-due");

    // return ResponseEntity.status(403).body(response);
    // }

    // // ✅ ALLOW LOGIN
    // response.put("roles", user.getRoles());
    // response.put("status", user.getStatus());

    // return ResponseEntity.ok(response);
    // }

    @GetMapping("/user/userList")
    public List<UserInfo> getUsers() {
        return userInfoRepository.findByRoles("ROLE_USER");
    }

    @GetMapping("/user/userListByOwnerGroup")
    public List<UserInfo> getOwnerUsers(@RequestParam String username) {
        return userInfoRepository.findUsersBySameOwnerGroup(username);
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
