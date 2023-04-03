package com.credpay.platform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    private static final long serialVersionUID= -3206518490338399368L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User>users;

    @ManyToMany(cascade = { CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="roles_authorities",
            joinColumns = @JoinColumn(name="roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="authorities_id", referencedColumnName = "id"))
    private Collection<Authority>authorities;

    public Role(){

    }
    public Role(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }
}