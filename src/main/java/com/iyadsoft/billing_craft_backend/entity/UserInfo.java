package com.iyadsoft.billing_craft_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info", indexes = {
    @Index(name = "idx_userinfo_username", columnList = "username"),
    @Index(name = "idx_userinfo_email", columnList = "email"),
    @Index(name = "idx_userinfo_status", columnList = "status"),
    @Index(name = "idx_userinfo_roles", columnList = "roles")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String password;
    private String roles;
    private String status;
}
