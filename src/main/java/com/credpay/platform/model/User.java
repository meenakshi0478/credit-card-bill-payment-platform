package com.credpay.platform.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID= -3206518490338399368L;

    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 120, unique = true)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(nullable = false, length = 25)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "users_roles",
            joinColumns = { @JoinColumn(name = "users_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "roles_id", referencedColumnName = "id") })
    private Collection<Role>roles;


}