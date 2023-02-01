package com.credpay.platform.model;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID= -3206518490338399368L;

    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(nullable = false, length = 120, unique = true)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(length = 25)
    private String phoneNumber;
//    @Column(nullable = false)
//    private String role;

    @Column(length = 120)
    private String address;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "users_roles",
            joinColumns = { @JoinColumn(name = "users_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "roles_id", referencedColumnName = "id") })
    private Collection<Role>roles;
   /* @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name="users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName = "id"))
    private Collection<Role>roles;*/

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public User(Long id, String userId, String firstName, String lastName, String email, String encryptedPassword, String phoneNumber, String role, String address, Boolean locked, Boolean enabled) {
        id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.phoneNumber = phoneNumber;
//        this.role = role;
//        this.address = address;
    }

    public User(String firstName, String lastName, String email, String password, String role) {

    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }

}