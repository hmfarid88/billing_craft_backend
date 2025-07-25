package com.iyadsoft.billing_craft_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyadsoft.billing_craft_backend.entity.LogoLink;
import com.iyadsoft.billing_craft_backend.entity.SmsPermission;
import com.iyadsoft.billing_craft_backend.repository.LogoLinkRepository;
import com.iyadsoft.billing_craft_backend.repository.SmsPermissionRepository;

@RestController
@RequestMapping("/smsapi/sms")
public class SmsPermissionController {
    @Autowired
    private SmsPermissionRepository smsPermissionRepository;

    @Autowired
    private LogoLinkRepository logoLinkRepository;

    @PutMapping("/update-status")
    public SmsPermission updateSmsStatus(@RequestParam String username, @RequestParam boolean status) {
        Optional<SmsPermission> smsPermissionOpt = smsPermissionRepository.findByUsername(username);
        if (smsPermissionOpt.isPresent()) {
            SmsPermission smsPermission = smsPermissionOpt.get();
            smsPermission.setStatus(status ? "ON" : "OFF");
            return smsPermissionRepository.save(smsPermission);
        } else {
            SmsPermission newPermission = new SmsPermission();
            newPermission.setUsername(username);
            newPermission.setQty(0);
            newPermission.setStatus("ON");
            return smsPermissionRepository.save(newPermission);
        }

    }

    @PutMapping("/update-smsqty")
    public SmsPermission addOrUpdateQty(String username, int newQty) {
        Optional<SmsPermission> existingPermission = smsPermissionRepository.findByUsername(username);

        if (existingPermission.isPresent()) {
            SmsPermission permission = existingPermission.get();
            permission.setQty(permission.getQty() + newQty);
            return smsPermissionRepository.save(permission);
        } else {
            SmsPermission newPermission = new SmsPermission();
            newPermission.setUsername(username);
            newPermission.setStatus("ON");
            newPermission.setQty(newQty);
            return smsPermissionRepository.save(newPermission);
        }
    }

    @GetMapping("/status")
    public SmsPermission getSmsPermissionByUsername(@RequestParam String username) {
        Optional<SmsPermission> smsPermissionOpt = smsPermissionRepository.findByUsername(username);
        if (smsPermissionOpt.isPresent()) {
            return smsPermissionOpt.get();
        }
        throw new RuntimeException("User not found");
    }

    @GetMapping("/allSmsUser")
    public ResponseEntity<List<SmsPermission>> getAllSmsPermissions() {
        List<SmsPermission> allPermissions = smsPermissionRepository.findAll();
        return ResponseEntity.ok(allPermissions);
    }

    @GetMapping("/allLogoUser")
    public ResponseEntity<List<LogoLink>> getAllLogoLink() {
        List<LogoLink> allLogo = logoLinkRepository.findAll();
        return ResponseEntity.ok(allLogo);
    }

    @PutMapping("/update-logolink")
    public LogoLink addOrUpdateLink(String username, String link) {
        Optional<LogoLink> existingLogo = logoLinkRepository.findByUsername(username);

        if (existingLogo.isPresent()) {
            LogoLink logoLink = existingLogo.get();
            logoLink.setLink(link);
            return logoLinkRepository.save(logoLink);
        } else {
            LogoLink logoLink = new LogoLink();
            logoLink.setUsername(username);
            logoLink.setLink(link);
            return logoLinkRepository.save(logoLink);
        }
    }

    @GetMapping("/getLogo")
    public Optional<LogoLink> findLogoByUsername(@RequestParam String username) {
        return logoLinkRepository.findByUsername(username);
    }

}
