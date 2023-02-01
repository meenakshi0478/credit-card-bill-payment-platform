package com.credpay.platform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue
    private Long Id;
    @Column(nullable = false, length = 50)
    private String adminId;
    @Column(nullable = false, length = 120, unique = true)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(nullable = false)
    private String role;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
